package ci.dgmp.sigomap.authmodule.model.dtos.appprivilege;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniquePrvName.UniqueRoleNameValidatorOnCreate.class})
@Documented
public @interface UniquePrvName
{
    String message() default "Privilège déjà créé";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnCreate implements ConstraintValidator<UniquePrvName, String>
    {
        private final PrvRepo prvRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !prvRepo.existsByName(value);
        }
    }
}


