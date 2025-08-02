package com.nagp.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "session")
public class SessionEntity {

    @Id
    private String session;

    @Column
    private String email;
}
