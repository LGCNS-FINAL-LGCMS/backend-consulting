package com.lgcms.consulting.common.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ConsultingError implements ErrorCodeInterface{
    DASHBOARD_DATA_NOT_FOUND("DASE-01","조회된 데이터가 없습니다.", HttpStatus.NOT_FOUND ),
    ;


    private final String status;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .status(status)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
