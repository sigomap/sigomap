package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConcordantPassword.ConcordantPasswordValidatorOnAccountActivation.class, ConcordantPassword.ConcordantPasswordValidatorOnChangingPassword.class, ConcordantPassword.ConcordantPasswordValidatorOnReinitPassword.class})
@Documented
public @interface ConcordantPassword
{
    String message() default "confirmPassword::Le mot de passe de confirmation doit être identique au mot de passe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ConcordantPasswordValidatorOnAccountActivation implements ConstraintValidator <ConcordantPassword, ActivateAccountDTO>
    {
        @Override
        public boolean isValid(ActivateAccountDTO dto, ConstraintValidatorContext context)
        {
            return dto.getPassword() == null || dto.getConfirmPassword() == null ? false : dto.getPassword().equals(dto.getConfirmPassword());
        }
    }

    @Component @RequiredArgsConstructor
    class ConcordantPasswordValidatorOnChangingPassword implements ConstraintValidator <ConcordantPassword, ChangePasswordDTO>
    {
        @Override
        public boolean isValid(ChangePasswordDTO dto, ConstraintValidatorContext context)
        {
            return dto.getNewPassword() == null || dto.getConfirmPassword() == null ? false : dto.getNewPassword().equals(dto.getConfirmPassword());
        }
    }

    @Component @RequiredArgsConstructor
    class ConcordantPasswordValidatorOnReinitPassword implements ConstraintValidator <ConcordantPassword, ReinitialisePasswordDTO>
    {
        @Override
        public boolean isValid(ReinitialisePasswordDTO dto, ConstraintValidatorContext context)
        {
            return dto.getNewPassword() == null || dto.getConfirmNewPassword() == null ? false : dto.getNewPassword().equals(dto.getConfirmNewPassword());
        }
    }
}


