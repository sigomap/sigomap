package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.FunctionRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UserHasAnyFunction.UserHasAnyFunctionValidatorOnlogin.class})
@Documented
public @interface UserHasAnyFunction
{
    String message() default "Vous ne disposez d'aucune fonction. Veuillez contacter un administrateur SVP";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UserHasAnyFunctionValidatorOnlogin implements ConstraintValidator<UserHasAnyFunction, LoginDTO>
    {
        private final FunctionRepo fncRepo;
        private final UserRepo userRepo;
        @Override
        public boolean isValid(LoginDTO dto, ConstraintValidatorContext context)
        {
            if(dto == null || dto.getUsername() == null || !userRepo.existsByEmail(dto.getUsername())) return true;
            return fncRepo.userHasAnyAppFunction(dto.getUsername());
        }
    }
}
