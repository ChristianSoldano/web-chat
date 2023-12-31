package ar.com.christiansoldano.chat.exception.chat.handler;

import ar.com.christiansoldano.chat.dto.ErrorDTO;
import ar.com.christiansoldano.chat.exception.chat.ChatAlreadyExistsException;
import ar.com.christiansoldano.chat.exception.chat.ChatNotBelongingException;
import ar.com.christiansoldano.chat.exception.chat.ChatNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class ChatExceptionHandler {

    @ExceptionHandler(ChatAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleChatAlreadyExists(ChatAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleChatNotFound(ChatNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), NOT_FOUND);
    }

    @ExceptionHandler(ChatNotBelongingException.class)
    public ResponseEntity<?> handleChatNotBelonging() {
        return new ResponseEntity<>(UNAUTHORIZED);
    }
}
