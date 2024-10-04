package ci.dgmp.sigomap.authmodule.model.dtos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ci.dgmp.sigomap.authmodule.controller.repositories.NatRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.UserRepo;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingCodePays.ExistingEmailValidator.class})
@Documented
public @interface ExistingCodePays
{
    String message() default "Pays non enregistré";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingEmailValidator implements ConstraintValidator<ExistingCodePays, String>
    {
        private final NatRepo natRepo;
        @Override
        public boolean isValid(String codePays, ConstraintValidatorContext context) {
            return natRepo.existsById(codePays);
        }
    }
}


