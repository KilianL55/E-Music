package edu.caensup.sio.emusic.service;

import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Gestionnaire;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoGestionnaire;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class DbUserLoginService implements UserDetailsService {

    @Autowired
    private IRepoResponsable repoResponsable;

    @Autowired
    private IRepoEnfant repoEnfant;

    @Autowired
    private IRepoGestionnaire repoGestionnaire;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Responsable> opt = Optional.ofNullable(repoResponsable.findByUsername(username));
        if (opt.isPresent()) {
            return opt.get();
        } else if (repoEnfant.findByUsername(username) != null) {
            return repoEnfant.findByUsername(username);
        } else if (repoGestionnaire.findByUsername(username) != null) {
            return repoGestionnaire.findByUsername(username);
        } else {
            throw new UsernameNotFoundException(username + " n'existe pas !");
        }
    }




}
