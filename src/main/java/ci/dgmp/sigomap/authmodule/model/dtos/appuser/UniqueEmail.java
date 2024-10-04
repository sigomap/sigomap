package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueEmail.NoneExistingEmailValidatorOnCreate.class, /*UniqueEmail.NoneExistingEmailValidatorOnUpdate.class*/})
@Documented
public @interface UniqueEmail
{
    String message() default "email::Adresse mail déjà attribuée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingEmailValidatorOnCreate implements ConstraintValidator<UniqueEmail, String>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !userRepo.alreadyExistsByEmail(value);
        }
    }
}




