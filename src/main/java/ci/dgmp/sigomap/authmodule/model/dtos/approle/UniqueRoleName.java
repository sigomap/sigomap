package ci.dgmp.sigomap.authmodule.model.dtos.approle;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.controller.repositories.RoleRepo;
import ci.dgmp.sigomap.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueRoleName.UniqueRoleNameValidatorOnCreate.class, UniqueRoleName.UniqueRoleNameValidatorOnUpdate.class})
@Documented
public @interface UniqueRoleName
{
    String message() default "Ce nom de rôle est déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnCreate implements ConstraintValidator<UniqueRoleName, String>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !roleRepo.existsByName(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnUpdate implements ConstraintValidator<UniqueRoleName, PrvsToRoleDTO>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(PrvsToRoleDTO dto, ConstraintValidatorContext context) {
            return !roleRepo.existsByName(dto.getRoleName(),dto.getRoleCode());
        }
    }
}


