package ci.dgmp.sigomap.authmodule.model.dtos.appprivilege;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneExistingPrivilegeCode.NoneExistingPrivilegeCodeValidator.class})
@Documented
public @interface NoneExistingPrivilegeCode
{
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingPrivilegeCodeValidator implements ConstraintValidator<NoneExistingPrivilegeCode, String>
    {
        private final PrvRepo prvDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return false;//!prvDAO.alreadyExistsByCode(value);
        }
    }
}
