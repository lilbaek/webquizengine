package engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "QuizCompleted")
public class QuizCompletedDbEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Column
    private Integer solveId;

    @Column
    private Integer id;

    @Column
    private LocalDateTime completedAt;

    @Column
    @JsonIgnore
    private String user;

    public Integer getSolveId() {
        return solveId;
    }

    public void setSolveId(Integer solveId) {
        this.solveId = solveId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static QuizCompletedDbEntry FromAnswer(QuizDbEntry dbEntry, String user) {
        var result = new QuizCompletedDbEntry();
        result.setId(dbEntry.getId());
        result.setCompletedAt(LocalDateTime.now());
        result.setUser(user);
        return result;
    }
}
