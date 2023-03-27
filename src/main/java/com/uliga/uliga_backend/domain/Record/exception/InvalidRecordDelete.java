package com.uliga.uliga_backend.domain.Record.exception;

public class InvalidRecordDelete extends IllegalArgumentException {
    public InvalidRecordDelete() {
        super("지출을 생성한 유저가 아니라서 삭제할 수 없습니다");
    }

    public InvalidRecordDelete(String s) {
        super(s);
    }
}
