package com.uliga.uliga_backend.domain.Member.model;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum UserLoginType {

    EMAIL(1,"EMAIL"),GOOGLE(2,"GOOGLE"),KAKAO(3,"KAKAO"),NAVER(4,"NAVER");

    private long id;
    private String type;

    UserLoginType(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public static UserLoginType valueOfLabel(String label) {
        return Arrays.stream(values())
                .filter(value -> value.type.equals(label))
                .findAny()
                .orElse(null);
    }

}
