package ci.dgmp.sigomap.archivemodule.model.dtos.validator;

import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidDocType.ValidArchiveTypeValidator.class})
@Documented
public @interface ValidDocType
{
    String message() default "Type d'archive invalide";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidArchiveTypeValidator implements ConstraintValidator<ValidDocType, String>
    {
        private final TypeRepo typeRepo;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return typeRepo.typeGroupHasChild(TypeGroup.DOCUMENT, value) ;
        }
    }
}
