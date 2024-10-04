package ci.dgmp.sigomap.authmodule.model.dtos.appprivilege;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingOrNullPrivilegeId.ExistingOrNullPrivilegeIdValidator.class})
@Documented
public @interface ExistingOrNullPrivilegeId
{
    String message() default "Invalid privilegeId";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullPrivilegeIdValidator implements ConstraintValidator<ExistingOrNullPrivilegeId, String>
    {
        private final PrvRepo prvRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return prvRepo.existsById(value) || value == null;
        }
    }
}
