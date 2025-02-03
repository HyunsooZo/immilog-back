package com.backend.immilog.user.domain.model.company;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.user.domain.enums.Industry;

public record Company(
        Long seq,
        Manager manager,
        CompanyData companyData
) {
    public static Company withNew() {
        return new Company(null, Manager.empty(), CompanyData.empty());
    }

    public Company seq(Long seq) {
        return new Company(seq, this.manager, this.companyData);
    }

    public Company manager(
            Country country,
            String region,
            Long userSeq
    ) {
        Manager manager = Manager.of(country, region, userSeq);
        return new Company(this.seq, manager, this.companyData);
    }

    public Company companyData(
            Industry industry,
            String name,
            String email,
            String phone,
            String address,
            String homepage,
            String logo
    ) {
        CompanyData companyData = CompanyData.of(
                industry,
                name,
                email,
                phone,
                address,
                homepage,
                logo
        );
        return new Company(this.seq, this.manager, companyData);
    }

    public Company updateName(String newName) {
        if (newName == null || this.companyData.name().equals(newName)) {
            return this;
        }
        return new Company(
                this.seq,
                this.manager,
                CompanyData.of(
                        this.companyData.industry(),
                        newName,
                        this.companyData.email(),
                        this.companyData.phone(),
                        this.companyData.address(),
                        this.companyData.homepage(),
                        this.companyData.logo()
                )
        );
    }

    public Company updatePhone(String newPhone) {
        if (newPhone == null || this.companyData.phone().equals(newPhone)) {
            return this;
        }
        return new Company(
                this.seq,
                this.manager,
                CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        this.companyData.email(),
                        newPhone,
                        this.companyData.address(),
                        this.companyData.homepage(),
                        this.companyData.logo()
                )
        );
    }

    public Company updateLogo(String newLogo) {
        if (newLogo == null || this.companyData.logo().equals(newLogo)) {
            return this;
        }
        return new Company(
                this.seq,
                this.manager,
                CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        this.companyData.email(),
                        this.companyData.phone(),
                        this.companyData.address(),
                        this.companyData.homepage(),
                        newLogo
                )
        );
    }

    public Company updateHomepage(String newHomepage) {
        if (newHomepage == null || this.companyData.homepage().equals(newHomepage)) {
            return this;
        }
        return new Company(
                this.seq,
                this.manager,
                CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        this.companyData.email(),
                        this.companyData.phone(),
                        this.companyData.address(),
                        newHomepage,
                        this.companyData.logo()
                )
        );
    }

    public Company updateEmail(String newEmail) {
        if (newEmail == null || this.companyData.email().equals(newEmail)) {
            return this;
        }
        return new Company(
                this.seq,
                this.manager,
                CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        newEmail,
                        this.companyData.phone(),
                        this.companyData.address(),
                        this.companyData.homepage(),
                        this.companyData.logo()
                )
        );
    }

    public Company updateCountry(Country newCountry) {
        if (newCountry == null || this.manager.country().equals(newCountry)) {
            return this;
        }
        return new Company(
                this.seq,
                Manager.of(
                        newCountry,
                        this.manager.region(),
                        this.manager.userSeq()
                ),
                this.companyData
        );
    }

    public Company updateAddress(String newAddress) {
        if (newAddress == null || this.companyData.address().equals(newAddress)) {
            return this;
        }
        return new Company(
                this.seq,
                this.manager,
                CompanyData.of(
                        this.companyData.industry(),
                        this.companyData.name(),
                        this.companyData.email(),
                        this.companyData.phone(),
                        newAddress,
                        this.companyData.homepage(),
                        this.companyData.logo()
                )
        );
    }

    public Company updateRegion(String newRegion) {
        if (newRegion == null || this.manager.region().equals(newRegion)) {
            return this;
        }
        return new Company(
                this.seq,
                Manager.of(
                        this.manager.country(),
                        newRegion,
                        this.manager.userSeq()
                ),
                this.companyData
        );
    }

    public Company updateIndustry(Industry industry) {
        if (industry == null || this.companyData.industry().equals(industry)) {
            return this;
        }
        return new Company(
                this.seq,
                this.manager,
                CompanyData.of(
                        industry,
                        this.companyData.name(),
                        this.companyData.email(),
                        this.companyData.phone(),
                        this.companyData.address(),
                        this.companyData.homepage(),
                        this.companyData.logo()
                )
        );
    }

    public Industry industry() {return this.companyData.industry();}

    public String name() {return this.companyData.name();}

    public String email() {return this.companyData.email();}

    public String phone() {return this.companyData.phone();}

    public String address() {return this.companyData.address();}

    public String homepage() {return this.companyData.homepage();}

    public Country country() {return this.manager.country();}

    public String region() {return this.manager.region();}

    public String logo() {return this.companyData.logo();}

    public Long managerUserSeq() {return this.manager.userSeq();}
    
}
