package co.edu.uniquindio.unieventos.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.uniquindio.unieventos.dto.exceptions.InvalidFieldsDTO;
import co.edu.uniquindio.unieventos.dto.exceptions.ErrorDTO;
import co.edu.uniquindio.unieventos.dto.exceptions.InvalidFieldDTO;

@RestControllerAdvice
public class ControllerExceptions {

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

}
