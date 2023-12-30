package ar.com.christiansoldano.chat.exception.chat;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(String message) {
        super(message);
    }
}
