package com.backend.immilog.post.domain.enums;

public enum Experience {
    JUNIOR("신입"),
    JUNIOR2("신입/경력"),
    MIDDLE("경력"),
    SENIOR("고급"),
    DIRECTOR("이사"),
    EXECUTIVE("임원"),
    ALL("전체");
    private final String experience;

    Experience(String experience) {this.experience = experience;}

    public String experience() {return experience;}
}