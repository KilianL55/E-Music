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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Responsable extends User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 50)
    private String nom;

    @Column(length = 50)
    private String prenom;

    @Column(length = 50)
    private String adresse;

    @Column(length = 50)
    private String ville;

    @Column(length = 50)
    private String adresse2="";

    @Column(length = 120)
    private String password;

    @Column(length = 5)
    private String code_postal;

    @Column(length = 50)
    private String username;

    @Column(length = 11)
    private int quotient_familial;

    @Column(length = 50)
    private String tel1;

    @Column(length = 50)
    private String tel2;

    @Column(length = 50)
    private String tel3;

    @Column()
    private int code_verification;

    @Column
    private boolean enabled=false;

    @OneToMany(mappedBy = "responsable")
    private List<Enfant> enfant;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Set<Cours> cours=new HashSet<>();


    private String authorities="RESPONSABLE"; // (2)

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

    //moyen de payement = payMethod (carte) et données de payement = payData (cryptogramme)

    @Column(length = 50)
    private String payMethod;

    @Column(length = 50)
    private String payData;

}
