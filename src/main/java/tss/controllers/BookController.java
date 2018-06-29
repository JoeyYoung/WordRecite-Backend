package tss.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import tss.annotations.session.Authorization;
import tss.annotations.session.CurrentUser;
import tss.entities.BookEntity;
import tss.entities.OrderEntity;
import tss.entities.TypeEntity;
import tss.entities.UserEntity;
import tss.repositories.BookRepository;
import tss.repositories.OrderRepository;
import tss.repositories.TypeRepository;
import tss.requests.information.*;
import tss.responses.information.*;

import java.util.*;


@Controller
@RequestMapping(path = "/book")
public class BookController {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final TypeRepository typeRepository;

    @Autowired
    public BookController(BookRepository bookRepository, OrderRepository orderRepository, TypeRepository typeRepository) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.typeRepository = typeRepository;
    }

    /**
     * Add a book binding to certain type
     *
     * @param user
     * @param request list of information of book
     * @return
     */
    @PostMapping(path = "/add")
    @Authorization
    public ResponseEntity<addBookResponse> addBook(@CurrentUser UserEntity user,
                                                   @RequestBody addBookRequest request) {
        BookEntity book = new BookEntity();
        Date now = new Date();
        String aname = request.getAuthorName();
        String desc = request.getDescription();
        String wordN = request.getTotalWords();
        String level = request.getLevel();
        String type = request.getType();

        Optional<TypeEntity> ret = typeRepository.findByName(type);
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new addBookResponse("no such type!", null, null), HttpStatus.BAD_REQUEST);
        }
        TypeEntity typeEntity = ret.get();
        typeEntity.setBookNum(typeEntity.getBookNum());

        book.setAddDate(now);
        book.setAuthorName(aname);
        book.setBelongedType(typeEntity);
        book.setDescr(desc);
        book.setWordNum(Integer.valueOf(wordN));
        book.setLevel(Integer.valueOf(level));

        bookRepository.save(book);
        return new ResponseEntity<>(new addBookResponse("add ok!", book.getId().toString(), book.getName()), HttpStatus.OK);
    }

    /**
     * The login user to order a book
     *
     * @param user
     * @param request book id
     * @return book name, user name
     */
    @PostMapping(path = "/order")
    @Authorization
    public ResponseEntity<orderBookResponse> orderBook(@CurrentUser UserEntity user,
                                                       @RequestBody orderBookRequest request) {
        Optional<BookEntity> ret = bookRepository.findById(Long.valueOf(request.getBookid()));
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new orderBookResponse("no such book", null, null), HttpStatus.BAD_REQUEST);
        }
        BookEntity book = ret.get();
        Long uid = Long.valueOf(user.getUid());
        Long bid = book.getId();

        /* deal with duplicated */
        Optional<OrderEntity> reto = orderRepository.findByUseridAndBookid(uid, bid);
        if (reto.isPresent()) {
            return new ResponseEntity<>(new orderBookResponse("already order", null, null), HttpStatus.BAD_REQUEST);
        }

        OrderEntity one = new OrderEntity();
        one.setBookid(uid);
        one.setUserid(bid);
        orderRepository.save(one);
        return new ResponseEntity<>(new orderBookResponse("order ok", book.getName(), user.getName()), HttpStatus.OK);
    }

    /**
     * The login user to unorder a book
     * @param user
     * @param request book id
     * @return status
     */
    @PostMapping(path = "/unorder")
    @Authorization
    public ResponseEntity<unOrderBookResponse> unorderBook(@CurrentUser UserEntity user,
                                                           @RequestBody orderBookRequest request){
        Long uid = Long.valueOf(user.getUid());
        Long bid = Long.valueOf(request.getBookid());
        Optional<OrderEntity> ret = orderRepository.findByUseridAndBookid(uid, bid);
        if(!ret.isPresent()){
            return new ResponseEntity<>(new unOrderBookResponse("no such order!"), HttpStatus.BAD_REQUEST);
        }
        OrderEntity order = ret.get();
        orderRepository.delete(order);
        return new ResponseEntity<>(new unOrderBookResponse("unorder ok"), HttpStatus.OK);
    }

    /**
     * Show the ordered book information for current user
     *
     * @param user
     * @return
     */
    @GetMapping(path = "/userorder")
    @Authorization
    public ResponseEntity<showAllBookResponse> showAllBook(@CurrentUser UserEntity user) {
        List<String> names = new ArrayList<>();
        List<String> words = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<String> descrs = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        List<OrderEntity> orders = orderRepository.findByUserid(Long.valueOf(user.getUid()));
        for (OrderEntity order : orders) {
           BookEntity book = bookRepository.findById(order.getBookid()).get();
           names.add(book.getName());
           words.add(book.getWordNum().toString());
           types.add(book.getBelongedType().getName());
           descrs.add(book.getDescr());
           dates.add(book.getAddDate().toString());
        }

        return new ResponseEntity<>(new showAllBookResponse(names, words, types, descrs, dates), HttpStatus.OK);
    }

}
