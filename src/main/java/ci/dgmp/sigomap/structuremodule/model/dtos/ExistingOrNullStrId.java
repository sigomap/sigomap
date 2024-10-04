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
@Constraint(validatedBy = {ExistingOrNullStrId.ExistingOrNullStrIdValidator.class})
@Documented
public @interface ExistingOrNullStrId
{
    String message() default "Invalid Structure Id";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullStrIdValidator implements ConstraintValidator<ExistingOrNullStrId, Long>
    {
        private final StrRepo strDAO;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return strDAO.existsById(value) ;
        }
    }
}
