package co.edu.uniquindio.unieventos.misc.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateTimeFutureValidator implements ConstraintValidator<ValidDateTimeFutureFormat, String> {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;

		try {
			LocalDateTime dateTime = LocalDateTime.parse(value, formatter);
			return !dateTime.isBefore(LocalDateTime.now());
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}
