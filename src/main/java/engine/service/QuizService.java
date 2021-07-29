package engine.service;

import engine.model.QuizDbEntry;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Page<QuizDbEntry> getAll(Integer pageNo) {
        Pageable paging = PageRequest.of(pageNo, 10);
        return quizRepository.findAll(paging);
    }
}
