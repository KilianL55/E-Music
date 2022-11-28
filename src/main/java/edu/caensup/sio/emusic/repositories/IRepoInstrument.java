package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.Instrument;
import org.springframework.data.repository.CrudRepository;

public interface IRepoInstrument extends CrudRepository<Instrument, Integer> {

}
