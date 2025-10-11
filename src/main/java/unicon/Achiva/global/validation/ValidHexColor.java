package unicon.Achiva.global.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import unicon.Achiva.global.validation.validator.HexColorValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HexColorValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHexColor {
    String message() default "유효한 색상 헥스 코드가 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
