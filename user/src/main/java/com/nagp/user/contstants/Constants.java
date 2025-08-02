package com.nagp.user.contstants;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Constants {
    public static final String DELIMITER = "@";
    private Constants() {}
}
