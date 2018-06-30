package tss.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tss.annotations.session.Authorization;
import tss.annotations.session.CurrentUser;
import tss.entities.BookEntity;
import tss.entities.UserEntity;
import tss.entities.WordEntity;
import tss.repositories.BookRepository;
import tss.repositories.TypeRepository;
import tss.repositories.WordRepository;
import tss.responses.information.*;
import tss.requests.information.*;

import java.util.*;

@Controller
@RequestMapping(path = "/word")
public class WordController {
    private final WordRepository wordRepository;
    private final BookRepository bookRepository;
    private final TypeRepository typeRepository;

    @Autowired
    public WordController(WordRepository wordRepository, BookRepository bookRepository, TypeRepository typeRepository) {
        this.wordRepository = wordRepository;
        this.bookRepository = bookRepository;
        this.typeRepository = typeRepository;
    }

    /**
     * Show all words stored
     *
     * @param user
     * @return chinese, english, book, type name;
     */
    @GetMapping(path = "/show")
    @Authorization
    public ResponseEntity<showAllWordResponse> showAllWord(@CurrentUser UserEntity user) {
        List<String> chineses = new ArrayList<>();
        List<String> englishs = new ArrayList<>();
        List<String> books = new ArrayList<>();
        List<String> types = new ArrayList<>();

        Iterator<WordEntity> itor = wordRepository.findAll().iterator();
        while (itor.hasNext()) {
            WordEntity word = itor.next();
            chineses.add(word.getChinese());
            englishs.add(word.getEnglish());
            books.add(word.getBelongedBook().getName());
            types.add(word.getBelongedBook().getBelongedType().getName());
        }
        return new ResponseEntity<>(new showAllWordResponse(chineses, englishs, books, types), HttpStatus.OK);
    }


    /**
     * Show the words of a certain book
     *
     * @param user
     * @param bid
     * @return chinese & english meanings
     */
    @GetMapping(path = "/bookword")
    @Authorization
    public ResponseEntity<showBookWordResponse> showBookWord(@CurrentUser UserEntity user,
                                                             @RequestParam String bid) {
        List<String> chineses = new ArrayList<>();
        List<String> englishs = new ArrayList<>();
        List<String> books = new ArrayList<>();
        List<String> types = new ArrayList<>();

        Optional<BookEntity> ret = bookRepository.findById(Long.valueOf(bid));
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new showBookWordResponse(null, null), HttpStatus.BAD_REQUEST);
        }

        BookEntity book = ret.get();
        Set<WordEntity> words = book.getWords();

        for (WordEntity word : words) {
            chineses.add(word.getChinese());
            englishs.add(word.getEnglish());
        }
        return new ResponseEntity<>(new showBookWordResponse(chineses, englishs), HttpStatus.OK);
    }

    /**
     * Search a word by the chinese meaning.
     *
     * @param user
     * @param chinese
     * @return list of chinese, english, book, type name
     */
    @GetMapping(path = "/search")
    @Authorization
    public ResponseEntity<searchWordResponse> searchByChin(@CurrentUser UserEntity user,
                                                           @RequestParam String chinese) {
        Optional<List<WordEntity>> ret = wordRepository.findByChinese(chinese);
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new searchWordResponse("no such word", null, null, null, null), HttpStatus.BAD_REQUEST);
        }

        List<String> chineses = new ArrayList<>();
        List<String> englishs = new ArrayList<>();
        List<String> books = new ArrayList<>();
        List<String> types = new ArrayList<>();

        for (WordEntity word : ret.get()) {
            chineses.add(word.getChinese());
            englishs.add(word.getEnglish());
            books.add(word.getBelongedBook().getName());
            types.add(word.getBelongedBook().getBelongedType().getName());
        }

        return new ResponseEntity<>(new searchWordResponse("find ok", chineses, englishs, books, types), HttpStatus.OK);
    }

    /**
     * add a word to certain book (not learning plan)
     *
     * @param user
     * @param request chinese, english, bid
     * @return status, word information
     */
    @PostMapping(path = "/add")
    @Authorization
    public ResponseEntity<addWordResponse> addWord(@CurrentUser UserEntity user,
                                                   @RequestBody addWordRequest request) {
        Optional<BookEntity> ret = bookRepository.findById(Long.valueOf(request.getBid()));
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new addWordResponse("no such book", null, null), HttpStatus.BAD_REQUEST);
        }

        BookEntity book = ret.get();
        WordEntity word = new WordEntity();
        word.setChinese(request.getChinese());
        word.setEnglish(request.getEnglish());
        word.setBelongedBook(book);
        book.setWordNum(book.getWordNum() + 1);
        bookRepository.save(book);
        wordRepository.save(word);
        return new ResponseEntity<>(new addWordResponse("add ok", request.getChinese(), request.getEnglish()), HttpStatus.OK);
    }

}
