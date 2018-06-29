package tss.repositories;

import org.springframework.data.repository.CrudRepository;
import tss.entities.WordEntity;

public interface WordRepository extends CrudRepository<WordEntity, Long> {

}
