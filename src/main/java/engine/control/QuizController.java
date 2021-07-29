package engine.control;

import engine.model.QuizAnswerDto;
import engine.model.QuizAnswerResultDto;
import engine.model.QuizCompletedDbEntry;
import engine.model.QuizCreateDto;
import engine.model.QuizDbEntry;
import engine.model.UserDetailsImpl;
import engine.repository.QuizCompletedRepository;
import engine.repository.QuizRepository;
import engine.service.QuizCompletedService;
import engine.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api")
@Validated
public class QuizController {
    private final QuizRepository quizRepository;
    private final QuizService quizService;
    private final QuizCompletedRepository quizCompletedRepository;
    private final QuizCompletedService quizCompletedService;

    @Autowired
    public QuizController(QuizRepository quizRepository, QuizService quizService, QuizCompletedRepository quizCompletedRepository, QuizCompletedService quizCompletedService) {
        this.quizRepository = quizRepository;
        this.quizService = quizService;
        this.quizCompletedRepository = quizCompletedRepository;
        this.quizCompletedService = quizCompletedService;
    }

    @GetMapping("/quizzes")
    public Page<QuizDbEntry> getQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return quizService.getAll(page);
    }

    @GetMapping("/quizzes/completed")
    public Page<QuizCompletedDbEntry> getQuizzesCompleted(@RequestParam(defaultValue = "0") Integer page, @AuthenticationPrincipal UserDetailsImpl user) {
        return quizCompletedService.getAll(page, user.getUsername());
    }

    @GetMapping("/quizzes/{id}")
    public QuizDbEntry getQuiz(@PathVariable Integer id) {
        return getQuizById(id);
    }

    @PostMapping("/quizzes")
    public QuizDbEntry createQuiz(@RequestBody @Valid QuizCreateDto quiz, @AuthenticationPrincipal UserDetailsImpl user) {
        return quizRepository.save(QuizDbEntry.CreateFromQuiz(quiz, user.getUsername()));
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl user) {
        var entity = getQuizById(id);
        if (!entity.getUser().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        quizRepository.delete(entity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/quizzes/{id}/solve")
    public QuizAnswerResultDto solveQuiz(@PathVariable Integer id, @RequestBody /*@Valid*/ QuizAnswerDto answer, @AuthenticationPrincipal UserDetailsImpl user) {
        var quiz = getQuizById(id);
        if (answer.getAnswer() == null) {
            answer.setAnswer(List.of());
        }
        var correctAnswers = new ArrayList<>(quiz.getAnswer());
        var providedAnswers = answer.getAnswer();
        if (correctAnswers.equals(providedAnswers)) {
            QuizAnswerResultDto quizAnswer = new QuizAnswerResultDto();
            quizAnswer.setSuccess(true);
            quizAnswer.setFeedback("Congratulations, you're right!");
            quizCompletedRepository.save(QuizCompletedDbEntry.FromAnswer(quiz, user.getUsername()));
            return quizAnswer;
        }
        QuizAnswerResultDto quizAnswer = new QuizAnswerResultDto();
        quizAnswer.setSuccess(false);
        quizAnswer.setFeedback("Wrong answer! Please, try again.");
        return quizAnswer;
    }

    private QuizDbEntry getQuizById(Integer id) {
        var quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "quiz not found");
        }
        return quiz.get();
    }
}
