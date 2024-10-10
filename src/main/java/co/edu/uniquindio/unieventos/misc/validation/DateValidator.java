package co.edu.uniquindio.unieventos.misc.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<ValidDateTimeFutureFormat, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;

		try {
			LocalDate.parse(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
