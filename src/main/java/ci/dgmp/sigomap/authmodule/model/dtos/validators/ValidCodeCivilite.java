package ci.dgmp.sigomap.authmodule.model.dtos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import ci.dgmp.sigomap.authmodule.controller.repositories.NatRepo;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidCodeCivilite.ExistingEmailValidator.class})
@Documented
public @interface ValidCodeCivilite
{
    String message() default "Civilité invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingEmailValidator implements ConstraintValidator<ValidCodeCivilite, String>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(String codeCivilite, ConstraintValidatorContext context) {
            if(codeCivilite == null) return true;
            return typeRepo.existsByGroupAndUniqueCode(TypeGroup.TYPE_CIVILITE, codeCivilite);
        }
    }
}


