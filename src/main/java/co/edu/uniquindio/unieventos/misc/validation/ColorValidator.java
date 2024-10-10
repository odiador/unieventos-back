package co.edu.uniquindio.unieventos.misc.validation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ColorValidator implements ConstraintValidator<ValidColor, String> {

	private static final Set<String> COLORS = new HashSet<>(Arrays.asList("white", "black", "red", "green", "blue",
			"yellow", "cyan", "magenta", "gray", "grey", "orange", "pink", "purple", "brown", "lightblue", "lightgreen",
			"darkblue", "darkred", "darkgreen", "lightgray", "darkgray"));

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;
		return COLORS.contains(value.toLowerCase()) || value.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
	}
}
