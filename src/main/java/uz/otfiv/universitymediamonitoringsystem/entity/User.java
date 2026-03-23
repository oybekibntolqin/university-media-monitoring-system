package uz.otfiv.universitymediamonitoringsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.otfiv.universitymediamonitoringsystem.entity.abs.AbsEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends AbsEntity implements UserDetails {
    private String fullName;
    private String phone;
    private String email;
    private LocalDate birthday;
    @OneToOne(cascade = CascadeType.ALL)
    private AttachmentContent attachmentContent;
    @ManyToOne
    private Organization organization;
    @JsonIgnore
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    private int failedAttempts;
    private LocalDateTime lockTime;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public String getBase64() {
        if (this.attachmentContent != null) {
            return Base64.getEncoder().encodeToString(this.attachmentContent.getContent());
        }
        return this.fullName;
    }
}
