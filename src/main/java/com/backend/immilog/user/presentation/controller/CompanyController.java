package com.backend.immilog.user.presentation.controller;

import com.backend.immilog.user.application.usecase.CompanyCreateUseCase;
import com.backend.immilog.user.application.usecase.CompanyFetchUseCase;
import com.backend.immilog.user.presentation.payload.CompanyPayload;
import com.backend.immilog.user.presentation.payload.UserGeneralResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Company API", description = "회사정보 관련 API")
@RequestMapping("/api/v1/companies")
@RestController
public class CompanyController {
    private final CompanyCreateUseCase.CompanyCreator companyCreator;
    private final CompanyFetchUseCase.CompanyFetcher companyFetcher;

    public CompanyController(
            CompanyCreateUseCase.CompanyCreator companyCreator,
            CompanyFetchUseCase.CompanyFetcher companyFetcher
    ) {
        this.companyCreator = companyCreator;
        this.companyFetcher = companyFetcher;
    }

    @PostMapping("/users/{userSeq}")
    @Operation(summary = "회사정보 등록", description = "회사정보를 등록합니다.")
    public ResponseEntity<UserGeneralResponse> registerCompany(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq,
            @RequestBody CompanyPayload.CompanyRegisterRequest param
    ) {
        companyCreator.registerOrEditCompany(userSeq, param.toCommand());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/users/{userSeq}")
    @Operation(summary = "본인 회사정보 조회", description = "본인 회사정보를 조회합니다.")
    public ResponseEntity<CompanyPayload.UserCompanyResponse> getCompany(
            @Parameter(description = "사용자 고유번호") @PathVariable("userSeq") Long userSeq
    ) {
        final var result = companyFetcher.getCompany(userSeq);
        return ResponseEntity.status(OK).body(result.toResponse());
    }
}
