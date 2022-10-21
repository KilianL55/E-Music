package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.Responsable;
import org.springframework.data.repository.CrudRepository;

public interface IRepoResponsable extends CrudRepository<Responsable,Integer> {
    public Responsable findByEmail(String email);
}
