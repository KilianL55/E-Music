package edu.caensup.sio.emusic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enfant extends User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 11)
    private int id;

    @Column(length = 50)
    private String nom;

    @Column(length = 50)
    private String prenom;

    private String date_naissance;

    @Column(length = 50)
    private String username;

    @Column(length = 120)
    private String password;

    @Column
    private boolean enabled;

    @Transient
    private String email_parent;

    @ManyToOne()
    private Responsable responsable= new Responsable();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<Cours> cours=new HashSet<>();

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

    @Override
    public Set<Cours> getCours() {
        return null;
    }
}
