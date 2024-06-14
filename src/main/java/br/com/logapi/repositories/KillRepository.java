package br.com.logapi.repositories;

import br.com.logapi.entities.KillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KillRepository extends JpaRepository<KillEntity, Long> {
    <S extends KillEntity> List<S> saveAll(Iterable<S> entities);
}
