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
@Constraint(validatedBy = {ExistingOrNullRoleId.ExistingOrNullRoleIdValidator.class})
@Documented
public @interface ExistingOrNullRoleId
{
    String message() default "Invalid roleCode";
    Class<?> [] group() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullRoleIdValidator implements ConstraintValidator<ExistingOrNullRoleId, String>
    {
        private final RoleRepo roleRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return roleRepo.existsById(value) || value==null;
        }
    }
}

