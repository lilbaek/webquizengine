package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Quiz")
public class QuizDbEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private String title;

    @Column
    private String text;

    @Column
    @JsonIgnore
    private String user;

    @Column
    @ElementCollection
    private List<String> options;

    @JsonIgnore
    @Column
    @ElementCollection
    private List<Integer> answer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static QuizDbEntry CreateFromQuiz(QuizCreateDto createDto, String user) {
        var created = new QuizDbEntry();
        created.setText(createDto.getText());
        created.setTitle(createDto.getTitle());
        created.setOptions(createDto.getOptions());
        created.setUser(user);
        if (createDto.getAnswer() == null) {
            created.setAnswer(List.of());
        } else {
            created.setAnswer(createDto.getAnswer());
        }
        return created;
    }
}
