package ci.dgmp.sigomap.authmodule.model.dtos.appfunction;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ci.dgmp.sigomap.authmodule.controller.repositories.FunctionRepo;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {UniqueFunctionName.UniqueFunctionNameValidatorOnCreate.class, UniqueFunctionName.UniqueFunctionNameValidatorOnUpdate.class})
public @interface UniqueFunctionName
{
    String message() default "Cette fonction existe déjà pour cet utilisateur";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueFunctionNameValidatorOnCreate implements ConstraintValidator<UniqueFunctionName, CreateFncDTO>
    {
        private final FunctionRepo functionRepo;
        @Override
        public boolean isValid(CreateFncDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null || dto.getUserId() == null || dto.getName() == null) return true;
            return !functionRepo.existsForUserByUserAndName(dto.getUserId(), dto.getName());
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueFunctionNameValidatorOnUpdate implements ConstraintValidator<UniqueFunctionName, UpdateFncDTO>
    {
        private final FunctionRepo functionRepo;
        @Override
        public boolean isValid(UpdateFncDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null || dto.getName() == null) return true;
            return !functionRepo.existsForUserByFncAndName(dto.getFncId(), dto.getName());
        }
    }
}
