package co.edu.uniquindio.unieventos.misc.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface ValidDateFormat {
	String message() default "Formato de fecha invalido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
