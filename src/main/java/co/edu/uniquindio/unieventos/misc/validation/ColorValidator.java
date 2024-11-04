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
		if (COLORS.contains(value.toLowerCase()))
			return true;
		String hexRegex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
		String rgbRegex = "^rgb\\((\\d{1,3}),\\s*(\\d{1,3}),\\s*(\\d{1,3})\\)$";

		if (value.toLowerCase().matches(hexRegex) || value.toLowerCase().matches(rgbRegex)) {
			if (value.toLowerCase().matches(rgbRegex)) {
				String[] rgbValues = value.replaceAll("[^0-9,]", "").split(",");
				for (String rgbValue : rgbValues) {
					int num = Integer.parseInt(rgbValue.trim());
					if (num < 0 || num > 255)
						return false;
				}
			}
			return true;
		}
		return false;
	}
}
