package edu.caensup.sio.emusic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enfant implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 11)
    private int id;

    @Column(length = 50)
    private String nom;

    @Column(length = 50)
    private String prenom;

    private Date date_naissance;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
    private String password;

    @Column
    private boolean enabled;

    @ManyToOne()
    private Responsable responsable;

    private String authorities="ENFANT"; // (2)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] auths = authorities.split(",");
        List<SimpleGrantedAuthority> authoritiesObjects = new ArrayList<SimpleGrantedAuthority>();
        for (String role : auths) {
            authoritiesObjects.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authoritiesObjects; // (3)
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
