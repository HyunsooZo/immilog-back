package com.backend.immilog.user.application.result;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.model.company.Company;
import com.backend.immilog.user.domain.model.company.CompanyData;
import com.backend.immilog.user.domain.model.company.Industry;
import com.backend.immilog.user.domain.model.company.Manager;
import com.backend.immilog.user.presentation.payload.CompanyPayload;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

public record CompanyResult(
        @Schema(description = "회사 식별자") Long seq,
        @Schema(description = "산업 분야") Industry industry,
        @Schema(description = "회사명") String companyName,
        @Schema(description = "회사 이메일") String companyEmail,
        @Schema(description = "회사 전화번호") String companyPhone,
        @Schema(description = "회사 주소") String companyAddress,
        @Schema(description = "회사 홈페이지") String companyHomepage,
        @Schema(description = "회사 국가") Country companyCountry,
        @Schema(description = "회사 지역") String companyRegion,
        @Schema(description = "회사 로고") String companyLogo,
        @Schema(description = "회사 관리자 식별자") Long companyManagerUserSeq
) {
    public static CompanyResult from(Company company) {
        return new CompanyResult(
                company.seq(),
                company.industry(),
                company.name(),
                company.email(),
                company.phone(),
                company.address(),
                company.homepage(),
                company.country(),
                company.region(),
                company.logo(),
                company.managerUserSeq()
        );
    }

    public Company toDomain() {
        return new Company(
                seq,
                Manager.of(
                        companyCountry,
                        companyRegion,
                        companyManagerUserSeq
                ),
                CompanyData.of(
                        industry,
                        companyName,
                        companyEmail,
                        companyPhone,
                        companyAddress,
                        companyHomepage,
                        companyLogo
                )
        );
    }

    public static CompanyResult createEmpty() {
        return new CompanyResult(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public CompanyPayload.UserCompanyResponse toResponse() {
        if (this.seq() == null) {
            return new CompanyPayload.UserCompanyResponse(
                    HttpStatus.NO_CONTENT.value(),
                    "회사 정보가 비어있습니다.",
                    null
            );
        } else {
            return new CompanyPayload.UserCompanyResponse(
                    HttpStatus.OK.value(),
                    "success",
                    this
            );
        }
    }
}