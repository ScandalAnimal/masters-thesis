package cz.vutbr.fit.maros.dip.exception;

public class CustomException extends RuntimeException {

    public CustomException(String exception) {
        super(exception);
    }

    public CustomException() {
        super();
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}


