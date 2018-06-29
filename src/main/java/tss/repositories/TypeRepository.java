package tss.repositories;

import org.springframework.data.repository.CrudRepository;
import tss.entities.TypeEntity;

import java.lang.reflect.Type;
import java.util.Optional;

public interface TypeRepository extends CrudRepository<TypeEntity, Long> {
    /**
     * find the type entity according to its name
     * @param name
     * @return Optional Type
     */
    Optional<TypeEntity> findByName(String name);
}
