package com.nagp.user.delegate;

import com.nagp.user.dto.CreateNewUserRequestDTO;
import com.nagp.user.exceptions.BadInputException;
import com.nagp.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDelegate {
    private static String INVALID_INPUT = "INVALID.INPUT";
    @Autowired
    private UserService userService;

    public String createNewUser(CreateNewUserRequestDTO createNewUserRequestDTO) {
        log.info("Inside UserDelegate :: createNewUser() method, request: {}", createNewUserRequestDTO);
        isValidRequest(createNewUserRequestDTO);
        return userService.createNewUser(createNewUserRequestDTO);
    }

    private void isValidRequest(CreateNewUserRequestDTO createNewUserRequestDTO) {
        if(StringUtils.isEmpty(createNewUserRequestDTO.getFirstName()))
            throw new BadInputException(INVALID_INPUT,"Please Enter firstName");
        if(StringUtils.isEmpty(createNewUserRequestDTO.getLastName()))
            throw new BadInputException(INVALID_INPUT,"Please Enter lastName");
        if(StringUtils.isEmpty(createNewUserRequestDTO.getEmail()) || !createNewUserRequestDTO.getEmail().contains("@") || !createNewUserRequestDTO.getEmail().contains(".com"))
            throw new BadInputException(INVALID_INPUT,"Enter valid email");
        if(StringUtils.isEmpty(createNewUserRequestDTO.getMobile()) || isInvalidMobileNumber(createNewUserRequestDTO.getMobile()))
            throw new BadInputException(INVALID_INPUT,"invalid mobile");
        if(StringUtils.isEmpty(createNewUserRequestDTO.getPassword()) || createNewUserRequestDTO.getPassword().length()<8)
            throw new BadInputException(INVALID_INPUT,"invalid password");
    }

    private boolean isInvalidMobileNumber(String mobile) {
        if(mobile.length() == 10 && mobile.matches("[0-9]+")) {
            return false;
        } else {
            return true;
        }
    }

    public String loginUser(String email, String password) {
        log.info("Inside UserDelegate :: loginUser() method, request: {}", email);
        if(StringUtils.isEmpty(email))
            throw new BadInputException(INVALID_INPUT,"Please Enter email");
        if(StringUtils.isEmpty(password) || password.length()<8)
            throw new BadInputException(INVALID_INPUT,"invalid password");
        return userService.loginUser(email,password);
    }

    public String logoutUser(String email, String token) {
        log.info("Inside UserDelegate :: logoutUser() method, request: {}", email);
        if(StringUtils.isEmpty(email))
            throw new BadInputException(INVALID_INPUT,"Please Enter username");
        if(StringUtils.isEmpty(token))
            throw new BadInputException(INVALID_INPUT,"Please Enter token");
        return userService.logoutUser(email, token);
    }

    public String forgotPassword(String email) {
        log.info("Inside UserDelegate :: forgotPassword() method, request: {}", email);
        if(StringUtils.isEmpty(email))
            throw new BadInputException(INVALID_INPUT,"Please Enter username");
        return userService.forgotPassword(email);
    }

    public String isValidToken(String token) {
        if(StringUtils.isEmpty(token))
            throw new BadInputException(INVALID_INPUT,"Please Enter token");
        userService.isValidSession(token);
        return "valid token";
    }
}
