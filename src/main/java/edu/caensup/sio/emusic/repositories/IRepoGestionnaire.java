package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.Gestionnaire;
import org.springframework.data.repository.CrudRepository;

public interface IRepoGestionnaire extends CrudRepository<Gestionnaire, Integer> {

    public Gestionnaire findByUsername(String username);

}

