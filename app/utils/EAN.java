package utils;
import models.Product;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = Product.EanValidator.class)

@Target({ElementType.FIELD })

@Retention(RetentionPolicy.RUNTIME)

public @interface EAN {

    String message() default "error.invalid.ean";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}