package edu.caensup.sio.emusic.repositories;

import org.springframework.stereotype.Repository;
import edu.caensup.sio.emusic.models.Responsable;

@Repository
public interface IRepoResponsable extends CustomRepository<Responsable, Integer> {
  public Responsable findByUsername(String username);

}
