package tss.controllers.bbs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tss.annotations.session.Authorization;
import tss.annotations.session.CurrentUser;
import tss.entities.UserEntity;
import tss.entities.bbs.BbsReplyEntity;
import tss.entities.bbs.BbsTopicEntity;
import tss.repositories.UserRepository;
import tss.repositories.bbs.BbsReplyRepository;
import tss.repositories.bbs.BbsTopicRepository;
import tss.requests.information.bbs.*;
import tss.responses.information.bbs.*;

import java.text.DateFormat;
import java.util.*;

@Controller
@RequestMapping(path = "/reply")
public class BbsReplyController {
    private final BbsTopicRepository bbsTopicRepository;
    private final UserRepository userRepository;
    private final BbsReplyRepository bbsReplyRepository;

    public BbsReplyController(BbsTopicRepository bbsTopicRepository, UserRepository userRepository, BbsReplyRepository bbsReplyRepository) {
        this.bbsTopicRepository = bbsTopicRepository;
        this.userRepository = userRepository;
        this.bbsReplyRepository = bbsReplyRepository;
    }

    /**
     * create a new reply to a topic/reply
     * request: tid, text, quoteIndex
     * permission: user in the section?
     * return: status
     * v1.0, done
     */
    @PostMapping(path = "/add")
    @Authorization
    public ResponseEntity<AddBbsReplyResponse> addReply(@CurrentUser UserEntity user,
                                                        @RequestBody AddBbsReplyRequest request) {
        /* permission error & invalid topic id error */
        long topicId = Long.valueOf(request.getTid());
        Optional<BbsTopicEntity> ret = bbsTopicRepository.findById(topicId);
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new AddBbsReplyResponse("no such topic"), HttpStatus.BAD_REQUEST);
        }

        BbsTopicEntity topic = ret.get();

        BbsReplyEntity reply = new BbsReplyEntity();


        reply.setAuthor(user);
        reply.setBelongedTopic(topic);


        reply.setContent(request.getText());

        Date time = new Date();
        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        mediumDateFormat.format(time);
        reply.setTime(time);

        /* set topic last reply time */
        reply.getBelongedTopic().setLastReplyTime(time);

        reply.setQuoteIndex(Integer.valueOf(request.getQuoteIndex()));
        reply.setIndex(topic.getReplyNum() + 1);

        String quoteIndex = request.getQuoteIndex();

        /* if quoted, the one quoted, unread ++
         * wait to be confirm
         */
        if (Integer.valueOf(quoteIndex) != 0) {
            /* do to the quoted one, unread ++ */
            BbsReplyEntity quoted = bbsReplyRepository.findByBelongedTopicAndIndex(topic, Integer.valueOf(quoteIndex));
            quoted.setUnread(quoted.getUnread() + 1);
            bbsReplyRepository.save(quoted);

            /* this reply unread */
            reply.setStatus(0);
        } else {
            reply.setStatus(-1);
        }

        /* the new reply has not been quoted */
        reply.setUnread(0);
        bbsReplyRepository.save(reply);

        /* need to add the reply number in the topic */
        topic.setReplyNum(topic.getReplyNum() + 1);

        bbsTopicRepository.save(topic);

        return new ResponseEntity<>(new AddBbsReplyResponse("add ok"), HttpStatus.OK);
    }


    /**
     * show all information under a certain topic
     * request: topic id, pages to show(10 per page)
     * return: see doc
     * v1.0, done
     */
    @PostMapping(path = "/info")
    public ResponseEntity<GetAllReplyResponse> getAllReplyInfo(@RequestBody GetAllReplyRequest request) {
        Optional<BbsTopicEntity> ret = bbsTopicRepository.findById(Long.valueOf(request.getTid()));
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new GetAllReplyResponse(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null), HttpStatus.BAD_REQUEST);
        }

        BbsTopicEntity topic = ret.get();

        String top;
        if (topic.getIsTop()) {
            top = "1";
        } else {
            top = "0";
        }

        String title = topic.getName();
        String totalPage = String.valueOf(topic.getReplyNum() / 10 + 1);
        String currentPage = request.getPage().toString();
        String postTime = topic.getTime().toString();
        String boardName = topic.getBelongedSection().getName();
        String boardID = String.valueOf(topic.getBelongedSection().getId());
        String topicID = String.valueOf(topic.getId());

        /* lz information */
        String lzid = topic.getAuthor().getUid();
        String lztext = topic.getContent();
        String lzphoto = topic.getAuthor().getIntro();
        String lztime = topic.getTime().toString();
        String lzname = topic.getAuthor().getName();


        List<String> ids = new ArrayList<>();
        List<String> texts = new ArrayList<>();
        List<String> quotes = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<String> photos = new ArrayList<>();
        List<String> indexs = new ArrayList<>();
        List<String> quoteAuthors = new ArrayList<>();
        List<String> quoteTimes = new ArrayList<>();
        List<String> quoteIndexs = new ArrayList<>();
        List<String> names = new ArrayList<>();


        /* all replied under the certain topic */
        String page = request.getPage();
        int index = (Integer.valueOf(page) - 1) * 10 + 1;
        for (; index <= Integer.valueOf(page) * 10 && index <= topic.getReplyNum(); index++) {

            /* current index information */
            BbsReplyEntity reply = bbsReplyRepository.findByBelongedTopicAndIndex(topic, index);
            UserEntity user = reply.getAuthor();
            ids.add(String.valueOf(reply.getId()));
            texts.add(reply.getContent());
            times.add(reply.getTime().toString());
            indexs.add(String.valueOf(index));

            /* current user information */
            photos.add(user.getIntro());
            names.add(user.getName());

            /* quoted reply information */
            Integer quoteIndex = reply.getQuoteIndex();
            /* no quote */
            if (quoteIndex == 0) {
                quotes.add("");
                quoteAuthors.add("");
                quoteTimes.add("");
                quoteIndexs.add("0");
                continue;
            }
            BbsReplyEntity quoteReply = bbsReplyRepository.findByBelongedTopicAndIndex(topic, quoteIndex);
            quotes.add(quoteReply.getContent());
            quoteAuthors.add(quoteReply.getAuthor().getName());
            quoteTimes.add(quoteReply.getTime().toString());
            quoteIndexs.add(quoteReply.getIndex().toString());
        }
        return new ResponseEntity<>(new GetAllReplyResponse(title, totalPage, currentPage, postTime, boardName, boardID, topicID, lzid, lztext, lzphoto, lztime, lzname, ids, texts, quotes, times, photos, indexs, quoteAuthors, quoteTimes, quoteIndexs, names, top), HttpStatus.OK);
    }
}
