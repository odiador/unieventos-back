package co.edu.uniquindio.unieventos.exceptions;

import java.util.List;

import lombok.Getter;

public class MultiErrorException extends Exception {

	@Getter
	private List<?> errors;
	@Getter
	private int code;

	public MultiErrorException(String msg, List<?> errors, int code) {
		super(msg);
		this.errors = errors;
		this.code = code;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
