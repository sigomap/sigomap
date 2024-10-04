package ci.dgmp.sigomap.authmodule.model.dtos.menu;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.MenuRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.FIELD) @Documented @Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueMenuName.UniqueMenuNameValidatorOnCreate.class})
public @interface UniqueMenuName
{
    String message() default "Nom de menu déjà attribué";
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};

    @Component @RequiredArgsConstructor
    class UniqueMenuNameValidatorOnCreate implements ConstraintValidator<UniqueMenuName, String>
    {
        private final MenuRepo menuRepo;
        @Override
        public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext)
        {
            return !menuRepo.existsByName(name);
        }
    }
}
