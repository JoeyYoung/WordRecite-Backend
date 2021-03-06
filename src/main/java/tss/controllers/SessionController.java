package tss.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import tss.entities.SessionEntity;
import tss.entities.UserEntity;
import tss.repositories.UserRepository;
import tss.requests.session.LoginRequest;
import tss.responses.session.LoginResponse;
import tss.repositories.SqlSessionRepository;
import tss.utils.SessionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Controller
@RequestMapping(path = "/session")
public class SessionController {
    private final UserRepository userRepository;

    private final SqlSessionRepository sqlSessionRepository;

    @Autowired
    public SessionController(UserRepository userRepository, SqlSessionRepository sqlSessionRepository) {
        this.userRepository = userRepository;
        this.sqlSessionRepository = sqlSessionRepository;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest login) {
        if (!userRepository.existsById(login.getUid())) {
            return new ResponseEntity<>(new LoginResponse("", "", null, "User not exists"), HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userRepository.findById(login.getUid()).get();
        if (!user.getPassword().equals(login.getPassword())) {
            return new ResponseEntity<>(new LoginResponse("", "", null, "Password incorrect"), HttpStatus.BAD_REQUEST);
        }

        SessionEntity session = new SessionEntity();
        session.setUid(login.getUid());
        session.setToken(SessionUtils.getToken());
        session.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        if (sqlSessionRepository.existsByUid(login.getUid())) {
            // TODO: process with duplicated insert.
            SessionEntity sessionToRemove = sqlSessionRepository.findByUid(login.getUid());
            sqlSessionRepository.delete(sessionToRemove);
        }

        sqlSessionRepository.save(session);

        return new ResponseEntity<>(new LoginResponse(login.getUid(), session.getToken(), user.getType(), "OK"), HttpStatus.OK);
    }
}
