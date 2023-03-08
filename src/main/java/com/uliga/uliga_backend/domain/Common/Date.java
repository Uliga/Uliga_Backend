package com.uliga.uliga_backend.domain.Common;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
public class Date {
    private Long year;

    private Long month;

    private Long day;
    @Builder
    public Date(Long year, Long month, Long day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date() {

    }
}
