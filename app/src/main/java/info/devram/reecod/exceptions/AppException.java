package info.devram.reecod.exceptions;

public class AppException extends RuntimeException {
    private Integer code;

    public AppException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
