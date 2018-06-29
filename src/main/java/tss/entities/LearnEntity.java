package tss.entities;

import javax.persistence.*;

@Entity
@Table(name = "learn")
public class LearnEntity {
    private Long id;
    private Long userId;
    private Long wordId;

    /**
     * 0- not remember, 1- remember
     * (all stored in the plan list)
     */
    private Integer status;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "learn_user")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "learn_word")
    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    @Column(name = "learn_status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
