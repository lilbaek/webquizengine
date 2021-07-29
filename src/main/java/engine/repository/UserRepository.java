package engine.repository;

import engine.model.UserDbEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserDbEntry, Long> {

    Optional<UserDbEntry> findByEmailIgnoreCase(String userName);
}
