package edu.caensup.sio.emusic.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Gestionnaire extends Responsable {

   private final String authorities="GESTIONNAIRE";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
