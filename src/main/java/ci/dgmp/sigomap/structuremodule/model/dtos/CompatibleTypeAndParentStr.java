package ci.dgmp.sigomap.structuremodule.model.dtos;

import ci.dgmp.sigomap.structuremodule.controller.repositories.StrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CompatibleTypeAndParentStr.CompatibleTypeAndParentStrValidatorOnCreate.class, CompatibleTypeAndParentStr.CompatibleTypeAndParentStrValidator.class})
@Documented
public @interface CompatibleTypeAndParentStr
{
    String message() default "Imcompatibilité de type : Impossible de loger une structure dans une tutelle de type inférieur";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
    @Component @RequiredArgsConstructor
    class CompatibleTypeAndParentStrValidator implements ConstraintValidator<CompatibleTypeAndParentStr, ChangeAnchorDTO>
    {
        private final StrRepo strRepo;
        @Override
        public boolean isValid(ChangeAnchorDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getNewParentId()==null) return true;
            return strRepo.parentHasCompatibleSousType(dto.getNewParentId(), dto.getNewTypeCode());
        }
    }

    @Component @RequiredArgsConstructor
    public class CompatibleTypeAndParentStrValidatorOnCreate implements ConstraintValidator <CompatibleTypeAndParentStr, CreateStrDTO>
    {
        private final StrRepo strRepo;
        @Override
        public boolean isValid(CreateStrDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getParentId()==null) return true;
            return strRepo.parentHasCompatibleSousType(dto.getParentId(), dto.getTypeCode());
        }
    }
}
