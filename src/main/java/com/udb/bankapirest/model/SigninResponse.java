package com.udb.bankapirest.model;

public class SigninResponse {
    private String message;
    private Boolean success;

    public SigninResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getSuccess() {
        return success;
    }
}
