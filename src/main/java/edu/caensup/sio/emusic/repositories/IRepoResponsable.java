package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.Cours;
import edu.caensup.sio.emusic.models.Responsable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRepoResponsable extends CrudRepository<Responsable,Integer> {
    public Responsable findByUsername(String username);

}
