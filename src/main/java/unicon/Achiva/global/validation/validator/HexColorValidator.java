package unicon.Achiva.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import unicon.Achiva.global.validation.ValidHexColor;

public class HexColorValidator implements ConstraintValidator<ValidHexColor, String> {
    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;  // null / 빈 문자열 허용 여부는 다른 제약과 조합
        }
        return value.matches(HEX_PATTERN);
    }
}
