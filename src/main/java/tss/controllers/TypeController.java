package tss.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tss.annotations.session.Authorization;
import tss.annotations.session.CurrentUser;
import tss.entities.TypeEntity;
import tss.entities.UserEntity;
import tss.repositories.BookRepository;
import tss.repositories.TypeRepository;
import tss.repositories.UserRepository;
import tss.responses.information.*;
import tss.requests.information.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/type")
public class TypeController {
    private final BookRepository bookRepository;
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;

    @Autowired
    public TypeController(BookRepository bookRepository, TypeRepository typeRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Add a type, init with book num to 0;
     *
     * @param user
     * @param request name, description
     * @return
     */
    @PostMapping(path = "/add")
    @Authorization
    public ResponseEntity<addTypeResponse> addType(@CurrentUser UserEntity user,
                                                   @RequestBody addTypeRequest request) {
        TypeEntity type = new TypeEntity();
        Optional<TypeEntity> ret = typeRepository.findByName(request.getName());
        if (ret.isPresent()) {
            return new ResponseEntity<>(new addTypeResponse("type exist!", null, null), HttpStatus.BAD_REQUEST);
        }

        type.setBookNum(0);
        type.setDescr(request.getDesc());
        type.setName(request.getName());

        typeRepository.save(type);
        return new ResponseEntity<>(new addTypeResponse("add ok!", type.getId().toString(), type.getName()), HttpStatus.OK);
    }

    /**
     * Delete a type according to the id
     *
     * @param user
     * @param request typeid
     * @return
     */
    @DeleteMapping(path = "/delete")
    @Authorization
    public ResponseEntity<deleteTypeResponse> deleteType(@CurrentUser UserEntity user,
                                                         @RequestBody deleteTypeRequest request) {
        Optional<TypeEntity> ret = typeRepository.findById(Long.valueOf(request.getId()));
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new deleteTypeResponse("no such id"), HttpStatus.BAD_REQUEST);
        }

        TypeEntity type = ret.get();
        typeRepository.delete(type);
        return new ResponseEntity<>(new deleteTypeResponse("delete ok"), HttpStatus.OK);
    }

    /**
     * List all type information in this web
     * @param user
     * @return names, descriptions, book numbers
     */
    @GetMapping(path = "/show")
    @Authorization
    public ResponseEntity<showAllTypeResponse> showAllType(@CurrentUser UserEntity user) {
        List<String> names = new ArrayList<>();
        List<String> descrs = new ArrayList<>();
        List<String> bookNum = new ArrayList<>();

        Iterator<TypeEntity> itor = typeRepository.findAll().iterator();
        while (itor.hasNext()) {
            TypeEntity type = itor.next();
            names.add(type.getName());
            descrs.add(type.getDescr());
            bookNum.add(type.getBookNum().toString());
        }
        return new ResponseEntity<>(new showAllTypeResponse(names, descrs, bookNum), HttpStatus.OK);
    }
}
