package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Responsable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IRepoEnfant extends CrudRepository<Enfant, Integer> {

    public Enfant findByUsername(String username);
    public List<Enfant> findByResponsable(Responsable responsable);
    public Enfant findByUsernameAndResponsable(String username, Responsable responsable);

}
