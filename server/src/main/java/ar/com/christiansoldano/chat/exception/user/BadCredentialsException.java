package ar.com.christiansoldano.chat.exception.user;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
