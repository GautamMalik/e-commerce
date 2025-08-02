package com.nagp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewUserRequestDTO {

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
