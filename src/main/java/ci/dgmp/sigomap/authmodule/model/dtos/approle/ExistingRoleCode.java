package ci.dgmp.sigomap.authmodule.model.dtos.approle;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingRoleCode.ExistingRoleIdValidator.class})
@Documented
public @interface ExistingRoleCode
{
    String message() default "Invalid roleId";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingRoleIdValidator implements ConstraintValidator<ExistingRoleCode, String>
    {
        private final RoleRepo roleRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return roleRepo.existsById(value);
        }
    }
}

