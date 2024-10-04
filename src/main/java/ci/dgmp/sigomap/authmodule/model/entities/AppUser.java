package ci.dgmp.sigomap.authmodule.model.entities;

import jakarta.persistence.*;
import ci.dgmp.sigomap.modulestatut.entities.Statut;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ci.dgmp.sigomap.typemodule.model.entities.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited @EntityListeners(AuditingEntityListener.class)
public class AppUser extends HistoDetails
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_GEN")
    @SequenceGenerator(name = "USER_ID_GEN", sequenceName = "USER_ID_GEN", allocationSize = 10)
    private Long userId;
    private String firstName;
    private String lastName;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String tel;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    @ManyToOne @JoinColumn(name = "GENRE_CODE")
    private Type civilite;
    @ManyToOne @JoinColumn(name = "nationalite_ID")
    private Nationalite nationalite;
    private String matriculeFonctionnaire;
    private int indiceFonctionnaire;
    private String nomPere;
    private String nomMere;

    private boolean active;
    private boolean notBlocked;
    private Long currentFunctionId;
    private LocalDateTime changePasswordDate;

    public AppUser(String firstName, String lastName, String password, String email, String tel, boolean active, boolean notBlocked) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.active = active;
        this.notBlocked = notBlocked;
    }

    public AppUser(Long userId, String firstName, String lastName, String password, String email, String tel, boolean active, boolean notBlocked, Long currentFunctionId, LocalDateTime changePasswordDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.active = active;
        this.notBlocked = notBlocked;
        this.currentFunctionId = currentFunctionId;
        this.changePasswordDate = changePasswordDate;
    }

    public AppUser(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(getUserId(), appUser.getUserId()) || Objects.equals(getEmail(), appUser.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, email, tel, active, notBlocked, currentFunctionId);
    }

    @Override
    public String toString() {
        return userId + "_" + email + "_" + firstName + "_" + lastName;
    }
}
