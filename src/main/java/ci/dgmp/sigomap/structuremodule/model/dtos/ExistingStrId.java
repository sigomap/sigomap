package ci.dgmp.sigomap.structuremodule.model.dtos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.structuremodule.controller.repositories.StrRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingStrId.ExistingStrIdValidator.class})
@Documented
public @interface ExistingStrId
{
    String message() default "Invalid Structure Id";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingStrIdValidator implements ConstraintValidator<ExistingStrId, Long>
    {
        private final StrRepo strDAO;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return strDAO.existsById(value);
        }
    }
}