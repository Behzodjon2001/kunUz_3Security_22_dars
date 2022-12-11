package com.company.dto.response;

import lombok.Data;

@Data
public class ResponseInfoDTO {
    private int status;
    private String massage;

    private String code;



    public ResponseInfoDTO(int status, String massage) {
        this.status = status;
        this.massage = massage;
    }

    public ResponseInfoDTO() {
    }
}
