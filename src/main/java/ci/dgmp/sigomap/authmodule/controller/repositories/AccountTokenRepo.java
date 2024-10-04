package ci.dgmp.sigomap.authmodule.controller.repositories;

import ci.dgmp.sigomap.authmodule.model.entities.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountTokenRepo extends JpaRepository<AccountToken, Long>
{
    boolean existsByToken(String token);
    Optional<AccountToken> findByToken(String token);

    @Query("select (count(t)>0) from AccountToken t where t.token = ?1 and t.user.userId = ?2")
    boolean existsByTokenAndUserId(String token, Long userId);

    @Query("select (count(t)>0) from AccountToken t where t.token = ?1 and t.user.userId = (select u.userId from AppUser  u where u.email = ?2)")
    boolean existsByTokenAndUserEmail(String token, String userEmail);

    @Query("select (count(t)>0) from AccountToken t where t.user.userId = ?1 and t.alreadyUsed = false and t.expirationDate >= current_timestamp ")
    boolean hasValidActivationToken(Long userId);

    @Query("select (count(t)>0) from AccountToken t where t.user.userId = ?1 and t.alreadyUsed = false and t.expirationDate <= current_timestamp and t.expirationDate = (select max(t2.expirationDate) from AccountToken t2 where t2.user.userId = ?1)")
    boolean lastActivationTokenHasExpired(Long userId);

    @Query("select t.user.userId from AccountToken t where t.token = ?1")
    Long getUserIdByToken(String token);
}
