package edu.caensup.sio.emusic.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import edu.caensup.sio.emusic.repositories.IRepoEnfant;
import edu.caensup.sio.emusic.repositories.IRepoResponsable;

public class DbUserLoginService implements UserDetailsService {

  @Autowired
  private IRepoResponsable repoResponsable;

  @Autowired
  private IRepoEnfant repoEnfant;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Responsable> opt = Optional.ofNullable(repoResponsable.findByUsername(username));
    if (opt.isPresent()) {
      return opt.get();
    } else {
      Optional<Enfant> optEnfant = Optional.ofNullable(repoEnfant.findByUsername(username));
      if (optEnfant.isPresent()) {
        return optEnfant.get();
      }
    }
    throw new UsernameNotFoundException(username + " n'existe pas !");
  }



}
