package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.AccountTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidToken.ValidTokenValidator.class, ValidToken.ValidTokenValidatorOnAccountActivation.class, ValidToken.ValidTokenValidatorOnReinitPassword.class})
@Documented
public @interface ValidToken
{
    String message() default "Lien invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidTokenValidator implements ConstraintValidator<ValidToken, String>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return value == null ? true : tokenRepo.existsByToken(value);
        }
    }

    @Component @RequiredArgsConstructor
    class ValidTokenValidatorOnAccountActivation implements ConstraintValidator <ValidToken, ActivateAccountDTO>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(ActivateAccountDTO dto, ConstraintValidatorContext context)
        {
            return tokenRepo.existsByTokenAndUserEmail(dto.getActivationToken(), dto.getEmail());
        }
    }

    @Component @RequiredArgsConstructor
    class ValidTokenValidatorOnReinitPassword implements ConstraintValidator <ValidToken, ReinitialisePasswordDTO>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(ReinitialisePasswordDTO dto, ConstraintValidatorContext context)
        {
            return tokenRepo.existsByTokenAndUserEmail(dto.getPasswordReinitialisationToken(), dto.getEmail());
        }
    }
}


