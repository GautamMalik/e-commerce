package com.nagp.user.service.impl;

import com.nagp.user.contstants.Constants;
import com.nagp.user.domain.SessionEntity;
import com.nagp.user.domain.UserEntity;
import com.nagp.user.dto.CreateNewUserRequestDTO;
import com.nagp.user.exceptions.BadInputException;
import com.nagp.user.repository.SessionRepository;
import com.nagp.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private List<UserEntity> userList = new ArrayList<>();

    @Autowired
    private SessionRepository sessionRepository;

    @PostConstruct
    public void insertDefaultUsers(){
        userList.add(UserEntity.builder().userId(userList.size()+1)
                .firstName("Gautam")
                .lastName("Malik")
                .email("gautam@gmail.com")
                .mobile("9876543210")
                .password("gautam123")
                .build());
    }

    @Override
    public String createNewUser(CreateNewUserRequestDTO createNewUserRequestDTO) {
        log.info("Inside UserServiceImpl :: createNewUser() method, request: {}", createNewUserRequestDTO);
        if (userList.stream().anyMatch(user -> user.getEmail().equals(createNewUserRequestDTO.getEmail()))) {
            throw new BadInputException("INVALID.INPUT","User with this email already exists");
        }
        UserEntity userEntity = UserEntity.builder().userId(userList.size()+1)
                                                    .firstName(createNewUserRequestDTO.getFirstName())
                                                    .lastName(createNewUserRequestDTO.getLastName())
                                                    .email(createNewUserRequestDTO.getEmail())
                                                    .mobile(createNewUserRequestDTO.getMobile())
                                                    .address(createNewUserRequestDTO.getAddress())
                                                    .city(createNewUserRequestDTO.getCity())
                                                    .state(createNewUserRequestDTO.getState())
                                                    .country(createNewUserRequestDTO.getCountry())
                                                    .password(createNewUserRequestDTO.getPassword())
                                                    .build();

        userList.add(userEntity);
        return "User created successfully with ID: " + userEntity.getUserId();
    }

    @Override
    public String loginUser(String email, String password) {
        log.info("Inside UserServiceImpl :: loginUser() method, username: {}", email);
        Optional<UserEntity> userEntityOptional = userList.stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst();

        SessionEntity sessionEntity = sessionRepository.findByEmail(email);
        if (sessionEntity != null) {
            return "session token : " + sessionEntity.getSession();
        }

        if (userEntityOptional.isPresent()) {
            String sessionToken = userEntityOptional.get().getUserId() + Constants.DELIMITER +  UUID.randomUUID();
            sessionRepository.save(new SessionEntity(sessionToken, userEntityOptional.get().getEmail()));
            return "session token : " + sessionToken;
        }
        throw new BadInputException("INVALID.INPUT","Invalid username or password");
    }

    @Override
    @Transactional
    public String logoutUser(String email, String token) {
        log.info("Inside UserServiceImpl :: logoutUser() method, username: {}", email);
        isValidSession(token);
        Optional<UserEntity> userEntityOptional = userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        if (userEntityOptional.isPresent()) {
            sessionRepository.deleteBySession(token);
            return "User logged out successfully";
        }
        throw new BadInputException("INVALID.INPUT","Invalid username");
    }

    @Override
    public String forgotPassword(String email) {
        log.info("Inside UserServiceImpl :: forgotPassword() method, username: {}", email);
        Optional<UserEntity> userEntityOptional = userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        if (userEntityOptional.isPresent()) {
            return "Password is : " + userEntityOptional.get().getPassword();
        }
        throw new BadInputException("INVALID.INPUT","Invalid username");
    }

    @Override
    public void isValidSession(String session){
        SessionEntity sessionEntity = sessionRepository.findBySession(session);
        if(sessionEntity == null){
            throw new BadInputException("INVALID.SESSION","Invalid session");
        }
    }
}
