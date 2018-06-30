package tss.repositories;

import org.springframework.data.repository.CrudRepository;
import tss.entities.LearnEntity;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface LearnRepository extends CrudRepository<LearnEntity, Long> {
    /**
     * Find current user's planned words
     *
     * @param uid user id Long
     * @param wid word id
     * @return learn entity
     */
    Optional<LearnEntity> findByUserIdAndWordId(Long uid, Long wid);

    /**
     * find current user's learned/ unlearned words
     * @param uid user id
     * @param status 1/0
     * @return List
     */
    List<LearnEntity> findByUserIdAndStatus(Long uid, Long status);

    /**
     * find current user's plan
     * @param uid user id
     * @return List
     */
    List<LearnEntity> findByUserId(Long uid);
}
