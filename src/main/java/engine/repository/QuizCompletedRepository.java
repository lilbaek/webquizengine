package engine.repository;

import engine.model.QuizCompletedDbEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCompletedRepository extends PagingAndSortingRepository<QuizCompletedDbEntry, Integer> {
    Page<QuizCompletedDbEntry> findAllByUserIgnoreCase(String userName, Pageable pageable);
}
