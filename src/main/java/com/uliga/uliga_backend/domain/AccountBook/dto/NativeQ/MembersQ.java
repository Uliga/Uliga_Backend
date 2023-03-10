package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MembersQ {
    private Long count;

    public MembersQ(Long count) {
        this.count = count;
    }
}
