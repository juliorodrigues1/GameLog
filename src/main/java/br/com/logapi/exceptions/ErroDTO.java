package br.com.logapi.exceptions;

public class ErroDTO {
    private String message;
    private int status;

    public ErroDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
