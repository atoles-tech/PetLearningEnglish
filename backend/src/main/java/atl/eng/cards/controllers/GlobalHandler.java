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
import atl.eng.cards.exceptions.cards.WordNotFoundInDictException;
import atl.eng.cards.exceptions.credential.CredentialAlreadyExists;
import atl.eng.cards.exceptions.dict.URIException;
import atl.eng.cards.exceptions.token.IncorrectTokenException;
import atl.eng.cards.exceptions.token.RefreshTokenNotFound;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(CardAlreadyExistsException.class)
    public ResponseEntity<?> handleCardAlreadyExists(CardAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(URIException.class)
    public ResponseEntity<?> handleURIException(URIException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WordNotFoundInDictException.class)
    public ResponseEntity<?> handleWordNotFoundInDictException(WordNotFoundInDictException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenNotFound.class)
    public ResponseEntity<?> handleRefreshTokenNotFound(RefreshTokenNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectTokenException.class)
    public ResponseEntity<?> handleIncorrectTokenException(IncorrectTokenException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CredentialAlreadyExists.class)
    public ResponseEntity<?> handleCredentialAlreadyExists(CredentialAlreadyExists ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CredentialNotFoundException.class)
    public ResponseEntity<?> handleCredentialNotFoundException(CredentialNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
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
