package co.edu.uniquindio.unieventos.config;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uniquindio.unieventos.dto.exceptions.ErrorDTO;
import co.edu.uniquindio.unieventos.dto.exceptions.InvalidFieldDTO;
import co.edu.uniquindio.unieventos.dto.exceptions.InvalidFieldsDTO;
import co.edu.uniquindio.unieventos.dto.exceptions.MultiErrorDTO;
import co.edu.uniquindio.unieventos.exceptions.ConflictException;
import co.edu.uniquindio.unieventos.exceptions.DelayException;
import co.edu.uniquindio.unieventos.exceptions.DeletedAccountException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.InvalidCodeException;
import co.edu.uniquindio.unieventos.exceptions.InvalidLoginException;
import co.edu.uniquindio.unieventos.exceptions.InvalidUsernameException;
import co.edu.uniquindio.unieventos.exceptions.MultiErrorException;
import co.edu.uniquindio.unieventos.exceptions.NotVerifiedAccountException;
import co.edu.uniquindio.unieventos.exceptions.UnauthorizedAccessException;

@RestControllerAdvice
public class ExceptionHandling {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleInvalidatedFields(MethodArgumentNotValidException ex) {
		return ResponseEntity.badRequest()
				.body(new InvalidFieldsDTO(400,
						ex.getBindingResult().getFieldErrors().stream()
								.map(e -> new InvalidFieldDTO(e.getField(), e.getDefaultMessage()))
								.collect(Collectors.toList())));
	}

	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<?> handlerException(InvalidLoginException e) {
		return ResponseEntity.status(401).body(new ErrorDTO(401, e.getMessage()));
	}

	@ExceptionHandler(InvalidUsernameException.class)
	public ResponseEntity<?> handlerException(InvalidUsernameException e) {
		return ResponseEntity.badRequest().body(new ErrorDTO(400, e.getMessage()));
	}

	@ExceptionHandler(DocumentNotFoundException.class)
	public ResponseEntity<?> handlerException(DocumentNotFoundException e) {
		return ResponseEntity.status(404).body(new ErrorDTO(404, e.getMessage()));
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<?> handlerException(ConflictException e) {
		return ResponseEntity.status(409).body(new ErrorDTO(409, e.getMessage()));
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<?> handlerException(UnauthorizedAccessException e) {
		return ResponseEntity.status(403).body(new ErrorDTO(403, e.getMessage()));
	}
	@ExceptionHandler(InvalidCodeException.class)
	public ResponseEntity<?> handlerException(InvalidCodeException e) {
		return ResponseEntity.status(400).body(new ErrorDTO(400, e.getMessage()));
	}

	@ExceptionHandler(DeletedAccountException.class)
	public ResponseEntity<?> handlerException(DeletedAccountException e) {
		return ResponseEntity.status(403).body(new ErrorDTO(403, e.getMessage()));
	}

	@ExceptionHandler(NotVerifiedAccountException.class)
	public ResponseEntity<?> handlerException(NotVerifiedAccountException e) {
		return ResponseEntity.status(410).body(new ErrorDTO(410, e.getMessage()));
	}

	@ExceptionHandler(DelayException.class)
	public ResponseEntity<?> handlerException(DelayException e) {
		return ResponseEntity.status(429).body(new ErrorDTO(429, e.getMessage()));
	}
	
	@ExceptionHandler(MultiErrorException.class)
	public ResponseEntity<?> handlerException(MultiErrorException e) {
		return ResponseEntity.status(e.getCode()).body(new MultiErrorDTO(e.getCode(), e.getMessage(), e.getErrors()));
	}


}
