package edu.caensup.sio.emusic.repositories;

import edu.caensup.sio.emusic.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRepoUser extends CrudRepository<User,Long> {
    public Optional<User> findByUsername(String username);

}
