package co.edu.uniquindio.unieventos.misc.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ObjectIdValidator.class)
public @interface ValidObjectId {

	String message() default "Id inválido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
