package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRepoEnfant extends CrudRepository<Enfant, Integer> {

    public Enfant findByUsername(String username);
    public List<Enfant> findByResponsable(Responsable responsable);

}
