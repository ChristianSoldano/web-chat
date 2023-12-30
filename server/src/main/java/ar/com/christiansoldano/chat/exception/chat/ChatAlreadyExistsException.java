package ar.com.christiansoldano.chat.exception.chat;

public class ChatAlreadyExistsException extends RuntimeException {

    public ChatAlreadyExistsException() {
        super("The chat already exists");
    }
}
