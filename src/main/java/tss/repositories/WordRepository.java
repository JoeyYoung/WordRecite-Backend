package tss.repositories;

import org.springframework.data.repository.CrudRepository;
import tss.entities.WordEntity;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends CrudRepository<WordEntity, Long> {
    /**
     * Search set of word by chinese
     *
     * @param chinese
     * @return optional
     */
    Optional<List<WordEntity>> findByChinese(String chinese);

    /**
     * search set of word by english meaning
     *
     * @param english
     * @return optional
     */
    Optional<List<WordEntity>> findByEnglish(String english);
}
