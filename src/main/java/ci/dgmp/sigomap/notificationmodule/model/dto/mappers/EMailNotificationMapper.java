package ci.dgmp.sigomap.notificationmodule.model.dto.mappers;

import ci.dgmp.sigomap.notificationmodule.model.entities.EmailNotification;
import ci.dgmp.sigomap.notificationmodule.model.dto.EmailNotificationDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EMailNotificationMapper
{
    EmailNotification mapToNotification(EmailNotificationDTO dto);
}
