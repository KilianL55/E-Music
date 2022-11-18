package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.Enfant;
import edu.caensup.sio.emusic.models.Facture;
import org.springframework.data.repository.CrudRepository;

public interface IRepoFacture extends CrudRepository<Facture, Integer> {
}
