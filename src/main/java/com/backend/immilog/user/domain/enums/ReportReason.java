package com.backend.immilog.user.domain.enums;

public enum ReportReason {
    INAPPROPRIATE_CONTENT("부적절한 내용"),
    SPAM("스팸 광고"),
    HATE_SPEECH("혐오 발언"),
    HARASSMENT("나체 이미지 또는 성적 불쾌감을 주는 행위"),
    VIOLENCE("폭력 또는 테러 등 위험한 행위"),
    FRAUD("사기 행위"),
    ILLEGAL_PRODUCT("불법 또는 규제 상품 판매"),
    OTHER("기타");

    private final String reason;

    ReportReason(String reason) {this.reason = reason;}

    public String reason() {return reason;}
}
