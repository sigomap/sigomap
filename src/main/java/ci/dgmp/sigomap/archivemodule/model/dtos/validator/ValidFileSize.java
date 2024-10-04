package ci.dgmp.sigomap.archivemodule.model.dtos.validator;

import ci.dgmp.sigomap.archivemodule.model.constants.DocumentsConstants;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFileSize.ValidFileSizeValidator.class})
@Documented
public @interface ValidFileSize
{
    String message() default "Fichier trop volumineux";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    class ValidFileSizeValidator implements ConstraintValidator<ValidFileSize, MultipartFile>
    {
        @Override
        public boolean isValid(MultipartFile file, ConstraintValidatorContext context)
        {
            if(file == null) return true;
            return file.getSize() <=  DocumentsConstants.UPLOAD_MAX_SIZE;
        }
    }
}
