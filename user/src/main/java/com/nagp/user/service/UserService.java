package com.nagp.user.service;

import com.nagp.user.dto.CreateNewUserRequestDTO;

public interface UserService {
    String createNewUser(CreateNewUserRequestDTO createNewUserRequestDTO);

    String loginUser(String email, String password);

    String logoutUser(String email, String token);

    String forgotPassword(String email);

    void isValidSession(String token);
}
