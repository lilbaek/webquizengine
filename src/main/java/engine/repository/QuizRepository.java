package engine.repository;

import engine.model.QuizDbEntry;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface QuizRepository extends PagingAndSortingRepository<QuizDbEntry, Integer> {

}
