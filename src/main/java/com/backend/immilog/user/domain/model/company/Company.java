package com.backend.immilog.user.domain.model.company;

import com.backend.immilog.user.application.command.CompanyRegisterCommand;
import com.backend.immilog.user.domain.enums.Industry;
import com.backend.immilog.user.domain.enums.UserCountry;
import lombok.Builder;

import java.util.Objects;
import java.util.stream.Stream;

public class Company {
    private final Long seq;
    private Manager manager;
    private CompanyData companyData;

    @Builder
    private Company(
            Long seq,
            Manager manager,
            CompanyData companyData
    ) {
        this.seq = seq;
        this.manager = manager;
        this.companyData = companyData;
    }

    public static Company of(
            Long userSeq,
            CompanyRegisterCommand request
    ) {
        return new Company(
                null,
                Manager.of(
                        request.companyCountry(),
                        request.companyRegion(),
                        userSeq
                ),
                CompanyData.of(
                        request.industry(),
                        request.companyName(),
                        request.companyEmail(),
                        request.companyPhone(),
                        request.companyAddress(),
                        request.companyHomepage(),
                        request.companyLogo()
                )
        );
    }

    public Company updateName(String newName) {
        Stream.of(this.companyData)
                .filter(Objects::nonNull)
                .filter(name -> !this.companyData.name().equals(newName))
                .findFirst()
                .ifPresent(name -> this.companyData = CompanyData.of(
                        this.companyData.industry(),
                        newName,
                        this.companyData.email(),
                        this.companyData.phone(),
                        this.companyData.address(),
                        this.companyData.homepage(),
                        this.companyData.logo()
                ));
        return this;
    }

    public Company updatePhone(String newPhone) {
        Stream.of(this.companyData)
                .filter(Objects::nonNull)
                .filter(phone -> !this.companyData.phone().equals(newPhone))
                .findFirst()
                .ifPresent(phone -> this.companyData = CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        this.companyData.email(),
                        newPhone,
                        this.companyData.address(),
                        this.companyData.homepage(),
                        this.companyData.logo()
                ));
        return this;
    }

    public Company updateLogo(String newLogo) {
        Stream.of(this.companyData)
                .filter(Objects::nonNull)
                .filter(logo -> !this.companyData.logo().equals(newLogo))
                .findFirst()
                .ifPresent(logo -> this.companyData = CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        this.companyData.email(),
                        this.companyData.phone(),
                        this.companyData.address(),
                        this.companyData.homepage(),
                        newLogo
                ));
        return this;
    }

    public Company updateHomepage(String newHomepage) {
        Stream.of(this.companyData)
                .filter(Objects::nonNull)
                .filter(homepage -> !this.companyData.homepage().equals(newHomepage))
                .findFirst()
                .ifPresent(homepage -> this.companyData = CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        this.companyData.email(),
                        this.companyData.phone(),
                        this.companyData.address(),
                        newHomepage,
                        this.companyData.logo()
                ));
        return this;
    }

    public Company updateEmail(String newEmail) {
        Stream.of(this.companyData)
                .filter(Objects::nonNull)
                .filter(email -> !this.companyData.email().equals(newEmail))
                .findFirst()
                .ifPresent(email -> this.companyData = CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        newEmail,
                        this.companyData.phone(),
                        this.companyData.address(),
                        this.companyData.homepage(),
                        this.companyData.logo()
                ));
        return this;
    }

    public Company updateCountry(UserCountry newCountry) {
        Stream.of(this.manager)
                .filter(Objects::nonNull)
                .filter(country -> !this.manager.country().equals(newCountry))
                .findFirst()
                .ifPresent(country -> this.manager = Manager.of(
                        newCountry,
                        this.manager.region(),
                        this.manager.UserSeq()
                ));
        return this;
    }

    public Company updateAddress(String newAddress) {
        Stream.of(this.companyData)
                .filter(Objects::nonNull)
                .filter(address -> !this.companyData.address().equals(newAddress))
                .findFirst()
                .ifPresent(address -> this.companyData = CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        this.companyData.email(),
                        this.companyData.phone(),
                        newAddress,
                        this.companyData.homepage(),
                        this.companyData.logo()
                ));
        return this;
    }

    public Company updateRegion(String newRegion) {
        Stream.of(this.manager)
                .filter(Objects::nonNull)
                .filter(region -> !this.manager.region().equals(newRegion))
                .findFirst()
                .ifPresent(region -> this.manager = Manager.of(
                        this.manager.country(),
                        newRegion,
                        this.manager.UserSeq()
                ));
        return this;
    }

    public Company updateIndustry(Industry industry) {
        Stream.of(this.companyData)
                .filter(Objects::nonNull)
                .filter(ind -> !this.companyData.industry().equals(industry))
                .findFirst()
                .ifPresent(ind -> this.companyData = CompanyData.of(
                        industry,
                        this.companyData.name(),
                        this.companyData.email(),
                        this.companyData.phone(),
                        this.companyData.address(),
                        this.companyData.homepage(),
                        this.companyData.logo()
                ));
        return this;
    }

    public Long getCompanySeq() {
        return this.seq;
    }

    public Industry getIndustry() {
        return this.companyData.industry();
    }

    public String getName() {
        return this.companyData.name();
    }

    public String getEmail() {
        return this.companyData.email();
    }

    public String getPhone() {
        return this.companyData.phone();
    }

    public String getAddress() {
        return this.companyData.address();
    }

    public String getHomepage() {
        return this.companyData.homepage();
    }

    public UserCountry getCountry() {
        return this.manager.country();
    }

    public String getRegion() {
        return this.manager.region();
    }

    public String getLogo() {
        return this.companyData.logo();
    }

    public Long getManagerUserSeq() {
        return this.manager.UserSeq();
    }
    
}
