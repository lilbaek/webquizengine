package engine.service;

import engine.model.QuizCompletedDbEntry;
import engine.repository.QuizCompletedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class QuizCompletedService {
    private final QuizCompletedRepository repository;

    @Autowired
    public QuizCompletedService(QuizCompletedRepository quizRepository) {
        this.repository = quizRepository;
    }

    public Page<QuizCompletedDbEntry> getAll(Integer pageNo, String username) {
        Pageable paging = PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, "completedAt"));

        return repository.findAllByUserIgnoreCase(username, paging);
    }
}
