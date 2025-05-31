package com.backend.immilog.user.application;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.application.usecase.UserRepostUseCase;
import com.backend.immilog.user.application.services.ReportCommandService;
import com.backend.immilog.user.application.services.UserCommandService;
import com.backend.immilog.user.application.services.ReportQueryService;
import com.backend.immilog.user.application.services.UserQueryService;
import com.backend.immilog.user.domain.model.report.ReportReason;
import com.backend.immilog.user.domain.model.user.UserStatus;
import com.backend.immilog.user.domain.model.user.*;
import com.backend.immilog.user.domain.service.UserReportPolicy;
import com.backend.immilog.user.exception.UserException;
import com.backend.immilog.user.presentation.payload.UserInformationPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDateTime;

import static com.backend.immilog.global.enums.UserRole.ROLE_USER;
import static com.backend.immilog.user.exception.UserErrorCode.ALREADY_REPORTED;
import static com.backend.immilog.user.exception.UserErrorCode.CANNOT_REPORT_MYSELF;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("사용자 신고 서비스 테스트")
class UserReportDataServiceTest {
    private final UserQueryService userQueryService = mock(UserQueryService.class);
    private final UserCommandService userCommandService = mock(UserCommandService.class);
    private final ReportCommandService reportCommandService = mock(ReportCommandService.class);
    private final ReportQueryService reportQueryService = mock(ReportQueryService.class);
    private final UserReportPolicy userReportPolicy = new UserReportPolicy(reportQueryService);
    private final UserRepostUseCase.UserReporter userReporter = new UserRepostUseCase.UserReporter(
            userQueryService,
            userCommandService,
            reportCommandService,
            userReportPolicy
    );

    @Test
    @DisplayName("사용자 신고 성공")
    void reportUser() {
        // given
        Long targetUserSeq = 1L;
        Long reporterUserSeq = 2L;
        //public record User(
        //        Long seq,
        //        Auth auth,
        //        UserRole userRole,
        //        ReportData reportData,
        //        Profile profile,
        //        Location location,
        //        UserStatus userStatus,
        //        LocalDateTime updatedAt
        //) {
        User user = new User(
                targetUserSeq,
                Auth.of("test@emial.com", "test"),
                ROLE_USER,
                ReportData.of(1L, Date.valueOf(LocalDateTime.now().toLocalDate())),
                Profile.of("test", "image", Country.SOUTH_KOREA),
                Location.of(Country.MALAYSIA, "KL"),
                UserStatus.PENDING,
                LocalDateTime.now()
        );
        UserInformationPayload.UserReportRequest reportUserRequest = new UserInformationPayload.UserReportRequest(ReportReason.FRAUD, "test");
        when(reportQueryService.existsByUserSeqNumbers(targetUserSeq, reporterUserSeq)).thenReturn(false);
        when(userQueryService.getUserById(targetUserSeq)).thenReturn(user);
        // when
        userReporter.reportUser(targetUserSeq, reporterUserSeq, reportUserRequest.toCommand());

        // then
        verify(reportCommandService, times(1)).save(any(com.backend.immilog.user.domain.model.report.Report.class));
    }

    @Test
    @DisplayName("사용자 신고 실패: 본인 신고")
    void reportUser_failed_himself() {
        // given
        Long targetUserSeq = 1L;
        Long reporterUserSeq = 1L;
        UserInformationPayload.UserReportRequest reportUserRequest = mock(UserInformationPayload.UserReportRequest.class);
        when(reportQueryService.existsByUserSeqNumbers(targetUserSeq, reporterUserSeq)).thenReturn(false);
        // when & then
        assertThatThrownBy(() -> userReporter.reportUser(
                targetUserSeq,
                reporterUserSeq,
                reportUserRequest.toCommand()
        ))
                .isInstanceOf(UserException.class)
                .hasMessage(CANNOT_REPORT_MYSELF.getMessage());
    }

    @Test
    @DisplayName("사용자 신고 실패: 중복 신고")
    void reportUser_failed_duplicated() {
        // given
        Long targetUserSeq = 1L;
        Long reporterUserSeq = 2L;
        UserInformationPayload.UserReportRequest reportUserRequest =  mock(UserInformationPayload.UserReportRequest.class);
        when(reportQueryService.existsByUserSeqNumbers(targetUserSeq, reporterUserSeq)).thenReturn(true);
        // when & then
        assertThatThrownBy(() -> userReporter.reportUser(
                targetUserSeq,
                reporterUserSeq,
                reportUserRequest.toCommand()
        ))
                .isInstanceOf(UserException.class)
                .hasMessage(ALREADY_REPORTED.getMessage());
    }
}