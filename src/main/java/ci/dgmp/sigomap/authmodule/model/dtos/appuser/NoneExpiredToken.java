package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.AccountTokenRepo;
import ci.dgmp.sigomap.authmodule.model.entities.AccountToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.time.LocalDateTime;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneExpiredToken.ValidTokenValidator.class})
@Documented
public @interface NoneExpiredToken
{
    String message() default "Le lien a expiré";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidTokenValidator implements ConstraintValidator<NoneExpiredToken, String>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            AccountToken token = tokenRepo.findByToken(value).orElse(null);
            return value == null ? false : token == null ? false : token.getExpirationDate().isAfter(LocalDateTime.now()) || token.getExpirationDate().equals(LocalDateTime.now());
        }
    }
}


