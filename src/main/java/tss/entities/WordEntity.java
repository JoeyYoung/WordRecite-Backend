package tss.entities;

import javax.persistence.*;

@Entity
@Table(name = "word")
public class WordEntity {
    private Long id;
    private String Chinese;
    private String English;

    private BookEntity belongedBook;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "word_chinese")
    public String getChinese() {
        return Chinese;
    }

    public void setChinese(String chinese) {
        Chinese = chinese;
    }

    @Column(name = "word_english")
    public String getEnglish() {
        return English;
    }

    public void setEnglish(String english) {
        English = english;
    }

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, optional = false)
    @JoinColumn(name = "book_id")
    public BookEntity getBelongedBook() {
        return belongedBook;
    }

    public void setBelongedBook(BookEntity belongedBook) {
        this.belongedBook = belongedBook;
    }
}
