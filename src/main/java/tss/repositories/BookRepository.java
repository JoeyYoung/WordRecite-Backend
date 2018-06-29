package tss.repositories;

import org.springframework.data.repository.CrudRepository;
import tss.entities.BookEntity;

public interface BookRepository extends CrudRepository<BookEntity, Long> {

}
