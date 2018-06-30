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
import tss.entities.LearnEntity;
import tss.entities.UserEntity;
import tss.entities.WordEntity;
import tss.repositories.LearnRepository;
import tss.repositories.WordRepository;
import tss.requests.information.*;
import tss.responses.information.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/learn")
public class LearnController {
    private final LearnRepository learnRepository;
    private final WordRepository wordRepository;

    @Autowired
    public LearnController(LearnRepository learnRepository, WordRepository wordRepository) {
        this.learnRepository = learnRepository;
        this.wordRepository = wordRepository;
    }

    /**
     * Add the word to current user learn plan list
     *
     * @param user    user id
     * @param request wid
     * @return status wid, chinese, english
     */
    @PostMapping(path = "/addplan")
    @Authorization
    public ResponseEntity<addWordPlanResponse> addWordPlan(@CurrentUser UserEntity user,
                                                           @RequestBody addWordPlanRequest request) {
        Long uid = Long.valueOf(user.getUid());
        Long wid = Long.valueOf(request.getWid());
        Optional<LearnEntity> ret = learnRepository.findByUserIdAndWordId(uid, wid);

        if (ret.isPresent()) {
            return new ResponseEntity<>(new addWordPlanResponse("word is already planned", null, null, null), HttpStatus.BAD_REQUEST);
        }

        Optional<WordEntity> retw = wordRepository.findById(wid);
        if (!retw.isPresent()) {
            return new ResponseEntity<>(new addWordPlanResponse("invalid word id", null, null, null), HttpStatus.BAD_REQUEST);
        }
        WordEntity word = retw.get();

        LearnEntity learn = new LearnEntity();
        learn.setUserId(uid);
        learn.setWordId(wid);
        learn.setStatus(0);
        learnRepository.save(learn);

        return new ResponseEntity<>(new addWordPlanResponse("add ok", wid.toString(), word.getChinese(), word.getEnglish()), HttpStatus.OK);
    }


    /**
     * move the word from current user's learn plan
     *
     * @param user    user id
     * @param request wid
     * @return status
     */
    @PostMapping(path = "/mvplan")
    @Authorization
    public ResponseEntity<statusResponse> moveWordPlan(@CurrentUser UserEntity user,
                                                       @RequestBody moveWordPlanRequest request) {
        Long uid = Long.valueOf(user.getUid());
        Long wid = Long.valueOf(request.getWid());
        Optional<LearnEntity> ret = learnRepository.findByUserIdAndWordId(uid, wid);

        if (!ret.isPresent()) {
            return new ResponseEntity<>(new statusResponse("word not planned"), HttpStatus.BAD_REQUEST);
        }

        Optional<WordEntity> retw = wordRepository.findById(wid);
        if (!retw.isPresent()) {
            return new ResponseEntity<>(new statusResponse("no such word"), HttpStatus.BAD_REQUEST);
        }
        WordEntity word = retw.get();

        LearnEntity delLearn = ret.get();
        learnRepository.delete(delLearn);
        return new ResponseEntity<>(new statusResponse("delete ok"), HttpStatus.OK);
    }

    /**
     * show all learned word in user's plan
     *
     * @param user user id
     * @return List of chin, english, status
     */
    @GetMapping(path = "/showlearn")
    @Authorization
    public ResponseEntity<showPlanWordResponse> showLearnWord(@CurrentUser UserEntity user) {
        Long uid = Long.valueOf(user.getUid());
        Long status = Long.valueOf(1);
        List<LearnEntity> learns = learnRepository.findByUserIdAndStatus(uid, status);

        List<String> chineses = new ArrayList<>();
        List<String> englishs = new ArrayList<>();
        List<String> statuss = new ArrayList<>();

        for (LearnEntity learn : learns) {
            WordEntity word = wordRepository.findById(learn.getWordId()).get();
            chineses.add(word.getChinese());
            englishs.add(word.getEnglish());
            statuss.add(status.toString());
        }
        return new ResponseEntity<>(new showPlanWordResponse(chineses, englishs, statuss), HttpStatus.OK);
    }


    /**
     * show all un learned word in user's plan
     *
     * @param user user id
     * @return List of chin, english, status
     */
    @GetMapping(path = "/showunlearn")
    @Authorization
    public ResponseEntity<showPlanWordResponse> showUnLearnWord(@CurrentUser UserEntity user) {
        Long uid = Long.valueOf(user.getUid());
        Long status = Long.valueOf(0);
        List<LearnEntity> learns = learnRepository.findByUserIdAndStatus(uid, status);

        List<String> chineses = new ArrayList<>();
        List<String> englishs = new ArrayList<>();
        List<String> statuss = new ArrayList<>();

        for (LearnEntity learn : learns) {
            WordEntity word = wordRepository.findById(learn.getWordId()).get();
            chineses.add(word.getChinese());
            englishs.add(word.getEnglish());
            statuss.add(status.toString());
        }
        return new ResponseEntity<>(new showPlanWordResponse(chineses, englishs, statuss), HttpStatus.OK);
    }


    /**
     * show all planned words of user
     *
     * @param user user id
     * @return List of chin, english, status
     */
    @GetMapping(path = "/show")
    @Authorization
    public ResponseEntity<showPlanWordResponse> showAllPlanWord(@CurrentUser UserEntity user) {
        Long uid = Long.valueOf(user.getUid());
        List<LearnEntity> learns = learnRepository.findByUserId(uid);

        List<String> chineses = new ArrayList<>();
        List<String> englishs = new ArrayList<>();
        List<String> statuss = new ArrayList<>();

        for (LearnEntity learn : learns) {
            WordEntity word = wordRepository.findById(learn.getWordId()).get();
            chineses.add(word.getChinese());
            englishs.add(word.getEnglish());
            statuss.add(learn.getStatus().toString());
        }
        return new ResponseEntity<>(new showPlanWordResponse(chineses, englishs, statuss), HttpStatus.OK);
    }

    /**
     * confirm to know a word in the test
     *
     * @param user    user id
     * @param request word id
     * @return status
     */
    @PostMapping(path = "/know")
    @Authorization
    public ResponseEntity<statusResponse> knowWord(@CurrentUser UserEntity user,
                                                   @RequestBody testWordRequest request) {
        Long uid = Long.valueOf(user.getUid());
        Long wid = Long.valueOf(request.getWid());
        Optional<LearnEntity> ret = learnRepository.findByUserIdAndWordId(uid, wid);
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new statusResponse("no such word"), HttpStatus.BAD_REQUEST);
        }

        LearnEntity learn = ret.get();
        learn.setStatus(1);
        learnRepository.save(learn);
        return new ResponseEntity<>(new statusResponse("confirm know the word"), HttpStatus.OK);
    }

    /**
     * confirm to unknow a word in the test
     *
     * @param user    user id
     * @param request word id
     * @return status
     */
    @PostMapping(path = "/unknow")
    @Authorization
    public ResponseEntity<statusResponse> unKnowWord(@CurrentUser UserEntity user,
                                                     @RequestBody testWordRequest request) {
        Long uid = Long.valueOf(user.getUid());
        Long wid = Long.valueOf(request.getWid());
        Optional<LearnEntity> ret = learnRepository.findByUserIdAndWordId(uid, wid);
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new statusResponse("no such word"), HttpStatus.BAD_REQUEST);
        }

        LearnEntity learn = ret.get();
        learn.setStatus(0);
        learnRepository.save(learn);
        return new ResponseEntity<>(new statusResponse("confirm unknow the word"), HttpStatus.OK);
    }
}
