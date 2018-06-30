package tss.controllers.bbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tss.annotations.session.Authorization;
import tss.annotations.session.CurrentUser;
import tss.entities.UserEntity;
import tss.entities.bbs.BbsSectionEntity;
import tss.repositories.UserRepository;
import tss.repositories.bbs.BbsSectionRepository;
import tss.requests.information.bbs.AddSectionNoticeRequest;
import tss.responses.information.bbs.*;

import java.util.*;

@Controller
@RequestMapping(path = "/section")
public class BbsSectionController {
    private final BbsSectionRepository bbsSectionRepository;
    private final UserRepository userRepository;

    @Autowired
    public BbsSectionController(BbsSectionRepository bbsSectionRepository, UserRepository userRepository) {
        this.bbsSectionRepository = bbsSectionRepository;
        this.userRepository = userRepository;
    }


    /**
     * show all sections information, no need permission
     * permission: anyone
     * return: List ids , List names
     * v1.0, done
     */
    @GetMapping(path = "/info")
    public ResponseEntity<GetInfoBbsSectionResponse> infoBbsSection() {
        List<String> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();

        Iterator<BbsSectionEntity> iter = bbsSectionRepository.findAll().iterator();
        while (iter.hasNext()) {
            BbsSectionEntity section = iter.next();
            Long id = section.getId();
            String name = section.getName();
            ids.add(id.toString());
            names.add(name);
        }


        /* empty in sections */
        if (ids.isEmpty()) {
            return new ResponseEntity<>(new GetInfoBbsSectionResponse(null, null), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new GetInfoBbsSectionResponse(ids, names), HttpStatus.OK);
    }


    /**
     * create a section notice
     * v1.0, done
     */
    @PostMapping(path = "/addnotice")
    @Authorization
    public ResponseEntity<AddSectionNoticeResponse> addSectionNotice(@CurrentUser UserEntity user,
                                                                     @RequestBody AddSectionNoticeRequest request) {
        Optional<BbsSectionEntity> ret = bbsSectionRepository.findById(Long.valueOf(request.getBoardID()));
        if (!ret.isPresent()) {
            return new ResponseEntity<>(new AddSectionNoticeResponse("no such section!"), HttpStatus.BAD_REQUEST);
        }

        /* check permission, only manager can do*/
//        if (!Config.TYPES[1].equals(user.readTypeName())) {
//            return new ResponseEntity<>(new AddSectionNoticeResponse("permission denied"), HttpStatus.FORBIDDEN);
//        }

        BbsSectionEntity section = ret.get();
        section.setNotice(request.getBoardText());

        bbsSectionRepository.save(section);
        return new ResponseEntity<>(new AddSectionNoticeResponse("add notice ok"), HttpStatus.OK);
    }
}
