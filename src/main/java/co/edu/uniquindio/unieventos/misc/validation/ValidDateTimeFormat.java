package co.edu.uniquindio.unieventos.misc.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeValidator.class)
public @interface ValidDateTimeFormat {
	String message() default "Formato de fecha inválido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
