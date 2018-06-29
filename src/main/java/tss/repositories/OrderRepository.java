package tss.repositories;

import org.springframework.data.repository.CrudRepository;
import tss.entities.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, Long>{
    /**
     * find the order entity according to user id and book id
     * @param uid
     * @param bid
     * @return Optional
     */
    Optional<OrderEntity> findByUseridAndBookid(Long uid, Long bid);

    /**
     * find all order information of a user
     * @param uid
     * @return orders
     */
    List<OrderEntity> findByUserid(Long uid);

    /**
     * find all users ordering certain book
     * @param bid
     * @return orders
     */
    List<OrderEntity> findByBookid(Long bid);
}
