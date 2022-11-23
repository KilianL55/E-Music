package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.Cours;
import edu.caensup.sio.emusic.models.Responsable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IRepoCour extends CrudRepository<Cours, Integer> {

    @Query(value = "SELECT * FROM \"responsable_cours\" WHERE \"responsable_id\" = :id", nativeQuery = true)
    Responsable findByResponsable(@Param("id") int id);
}
