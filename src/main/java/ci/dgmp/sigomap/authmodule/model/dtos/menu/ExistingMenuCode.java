package ci.dgmp.sigomap.authmodule.model.dtos.menu;

import ci.dgmp.sigomap.authmodule.controller.repositories.MenuRepo;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.FIELD) @Documented @Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingMenuCode.ExistingMenuCodeValidatorOnCreate.class})
public @interface ExistingMenuCode
{
    String message() default "Code de menu inconnu";
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};

    @Component @RequiredArgsConstructor
    class ExistingMenuCodeValidatorOnCreate implements ConstraintValidator<ExistingMenuCode, String>
    {
        private final MenuRepo menuRepo;
        @Override
        public boolean isValid(String menuCode, ConstraintValidatorContext constraintValidatorContext)
        {
            return menuRepo.existsByMenuCode(menuCode);
        }
    }
}
