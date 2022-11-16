package edu.caensup.sio.emusic.repositories;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
  void refresh(T t);

  void attach(T t);

  public T refreshDetached(T entity, ID id);
}
