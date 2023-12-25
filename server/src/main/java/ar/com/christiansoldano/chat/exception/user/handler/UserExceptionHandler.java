package ar.com.christiansoldano.chat.exception.user.handler;

import ar.com.christiansoldano.chat.dto.ErrorDTO;
import ar.com.christiansoldano.chat.exception.user.BadCredentialsException;
import ar.com.christiansoldano.chat.exception.user.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(UserAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), UNAUTHORIZED);
    }
}
