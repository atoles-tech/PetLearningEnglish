package atl.eng.cards.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.CredentialNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import atl.eng.cards.exceptions.auth.IncorrectPasswordException;
import atl.eng.cards.exceptions.cards.CardAlreadyExistsException;
import atl.eng.cards.exceptions.cards.WordNotFoundException;
import atl.eng.cards.exceptions.cards.WordNotFoundInDictException;
import atl.eng.cards.exceptions.credential.CredentialAlreadyExists;
import atl.eng.cards.exceptions.dict.URIException;
import atl.eng.cards.exceptions.token.IncorrectTokenException;
import atl.eng.cards.exceptions.token.RefreshTokenNotFound;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({
        CardAlreadyExistsException.class,
        CredentialAlreadyExists.class
    })
    public ResponseEntity<?> handlExistsExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
        WordNotFoundInDictException.class,
        WordNotFoundException.class,
        RefreshTokenNotFound.class,
        CredentialNotFoundException.class,
    })
    public ResponseEntity<?> handlNotFoundExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(URIException.class)
    public ResponseEntity<?> handleURIException(URIException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectTokenException.class)
    public ResponseEntity<?> handleIncorrectTokenException(IncorrectTokenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handleIncorrectPasswordException(IncorrectPasswordException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
