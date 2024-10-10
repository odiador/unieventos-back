package co.edu.uniquindio.unieventos.misc.validation;

import org.bson.types.ObjectId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ObjectIdValidator implements ConstraintValidator<ValidObjectId, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;
		return ObjectId.isValid(value);
	}
}
