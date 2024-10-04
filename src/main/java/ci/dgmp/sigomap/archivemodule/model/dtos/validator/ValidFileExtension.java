package ci.dgmp.sigomap.archivemodule.model.dtos.validator;

import ci.dgmp.sigomap.archivemodule.model.constants.DocumentsConstants;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UploadDocReq;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidFileExtension.ValidFileExtensionValidator.class,
        ValidFileExtension.ValidFileExtensionValidatorOnDTO.class, ValidFileExtension.ValidFileExtensionValidatorOnBase64Url.class})
@Documented
public @interface ValidFileExtension
{
    String message() default "Type de fichier non pris en charge";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidFileExtensionValidator implements ConstraintValidator<ValidFileExtension, MultipartFile>
    {
        @Override
        public boolean isValid(MultipartFile file, ConstraintValidatorContext context)
        {
            if(file == null) return true;
            if(file.getOriginalFilename().equals("")) return true;
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            List<String> authorizedExtensions =  DocumentsConstants.DOCUMENT_AUTHORIZED_TYPE.stream().map(String::toLowerCase).collect(Collectors.toList());
            return authorizedExtensions.contains(extension.toLowerCase()) ;
        }
    }

    @Component @RequiredArgsConstructor
    class ValidFileExtensionValidatorOnBase64Url implements ConstraintValidator<ValidFileExtension, String>
    {
        @Override
        public boolean isValid(String extension, ConstraintValidatorContext context)
        {
            List<String> authorizedExtensions =  DocumentsConstants.DOCUMENT_AUTHORIZED_TYPE.stream().map(String::toLowerCase).collect(Collectors.toList());
            return authorizedExtensions.contains(extension.toLowerCase()) ;
        }
    }

    @Component @RequiredArgsConstructor
    class ValidFileExtensionValidatorOnDTO implements ConstraintValidator<ValidFileExtension, UploadDocReq>
    {
        @Override
        public boolean isValid(UploadDocReq dto, ConstraintValidatorContext context)
        {
            MultipartFile file = dto.getFile();
            if(file == null) return true;
            if(file.getOriginalFilename().equals("")) return true;
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            if(dto.getDocUniqueCode() == null) return true;
            if(dto.getDocUniqueCode().equals("PHT"))
            {
                return DocumentsConstants.PHOTO_AUTHORIZED_TYPE.contains(extension.toLowerCase());
            }
            return DocumentsConstants.DOCUMENT_AUTHORIZED_TYPE.contains(extension.toLowerCase());
        }
    }
}
