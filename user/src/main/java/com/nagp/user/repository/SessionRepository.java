package com.nagp.user.repository;

import com.nagp.user.domain.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    SessionEntity findBySession(String session);

    SessionEntity findByEmail(String email);

    void deleteBySession(String session);
}
