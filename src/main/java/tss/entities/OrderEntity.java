package tss.entities;

import javax.persistence.*;

@Entity
@Table(name = "bookorder")
public class OrderEntity {
    private Long id;
    private Long bookid;
    private Long userid;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "order_book")
    public Long getBookid() {
        return bookid;
    }

    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }

    @Column(name = "order_user")
    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
