package co.edu.uniquindio.unieventos.exceptions;

import lombok.Getter;

public class BadRequestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Getter
	private String field;

	public BadRequestException(String msg, String field) {
		super(msg);
		this.field = field;
	}
}
