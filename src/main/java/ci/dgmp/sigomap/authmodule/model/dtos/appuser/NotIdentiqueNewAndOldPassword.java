package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.UserRepo;
import ci.dgmp.sigomap.authmodule.model.entities.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NotIdentiqueNewAndOldPassword.NotIdentiqueNewAndOldPasswordValidatorOnChangePassword.class, NotIdentiqueNewAndOldPassword.NotIdentiqueNewAndOldPasswordValidatorOnReinitPassword.class})
@Documented
public @interface NotIdentiqueNewAndOldPassword
{
    String message() default "Le nouveau mot de passe doit être différent de l'ancien";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class NotIdentiqueNewAndOldPasswordValidatorOnChangePassword implements ConstraintValidator<NotIdentiqueNewAndOldPassword, ChangePasswordDTO>
    {
        private final UserRepo userRepo;
        private final PasswordEncoder passwordEncoder;
        @Override
        public boolean isValid(ChangePasswordDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getUserId() == null) return true;
            AppUser user = userRepo.findById(dto.getUserId()).orElse(null);
            if(user == null || user.getPassword() == null || dto.getNewPassword() == null) return true;
             return !passwordEncoder.matches(dto.getNewPassword(), user.getPassword());
        }
    }

    @Component
    @RequiredArgsConstructor
    class NotIdentiqueNewAndOldPasswordValidatorOnReinitPassword implements ConstraintValidator<NotIdentiqueNewAndOldPassword, ReinitialisePasswordDTO>
    {
        private final UserRepo userRepo;
        private final PasswordEncoder passwordEncoder;
        @Override
        public boolean isValid(ReinitialisePasswordDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getEmail() == null) return true;
            AppUser user = userRepo.findByEmail(dto.getEmail()).orElse(null);
            if(user == null || user.getPassword() == null || dto.getNewPassword() == null) return true;
            return !passwordEncoder.matches(dto.getNewPassword(), user.getPassword());
        }
    }
}
