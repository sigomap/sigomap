package ci.dgmp.sigomap.authmodule.model.dtos.appfunction;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.FunctionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingFncId.ExistingFncIdValidator.class})
public @interface ExistingFncId
{
    String message() default "Identitifiant de la fonction est invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingFncIdValidator implements ConstraintValidator<ExistingFncId, Long>
    {
        private final FunctionRepo assRepo;
        @Override
        public boolean isValid(Long fncId, ConstraintValidatorContext context)
        {
            return assRepo.existsById(fncId);
        }
    }
}
