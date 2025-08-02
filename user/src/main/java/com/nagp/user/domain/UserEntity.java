package com.nagp.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobile;
    private String address;
    private String city;
    private String state;
    private String country;

}
