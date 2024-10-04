package ci.dgmp.sigomap.notificationmodule.controller.dao;

import ci.dgmp.sigomap.notificationmodule.model.entities.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailNotificationRepo extends JpaRepository<EmailNotification, Long>
{
    Optional<EmailNotification> findByToken(String token);

}