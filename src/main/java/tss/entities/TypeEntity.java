package tss.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "type")
public class TypeEntity {
    private Long id;
    private String name;
    private String descr;
    private Integer bookNum;

    private Set<BookEntity> books;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "type_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "type_description")
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }


    @Column(name = "type_booknumber")
    public Integer getBookNum() {
        return bookNum;
    }

    public void setBookNum(Integer bookNum) {
        this.bookNum = bookNum;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "belongedType")
    public Set<BookEntity> getBooks() {
        return books;
    }

    public void setBooks(Set<BookEntity> books) {
        this.books = books;
    }

}
