package ci.dgmp.sigomap.authmodule.model.dtos.asignation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CoherentDates.CoherentDatesValidatorOnCreate.class,
        CoherentDates.CoherentDatesValidatorOnUpdate.class})
public @interface CoherentDates
{
    String message() default "La date de début ne peut être ultérieure à la date de fin";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnCreate implements ConstraintValidator<CoherentDates, CreateFunctionDTO>
    {
        @Override
        public boolean isValid(CreateFunctionDTO dto, ConstraintValidatorContext context)
        {
            return dto.getStartsAt() == null || dto.getEndsAt() == null ? true : dto.getStartsAt().isBefore(dto.getEndsAt()) || dto.getStartsAt().isEqual(dto.getEndsAt());
        }
    }

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnUpdate implements ConstraintValidator<CoherentDates, UpdateFncDTO>
    {
        @Override
        public boolean isValid(UpdateFncDTO dto, ConstraintValidatorContext context)
        {
            return dto.getStartsAt() == null || dto.getEndsAt() == null ? true : dto.getStartsAt().isBefore(dto.getEndsAt()) || dto.getStartsAt().isEqual(dto.getEndsAt());
        }
    }
}
