package com.dera.AuthFortress.repository;

import com.dera.AuthFortress.domain.entities.TheUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TheUser, Long> {
    Optional<TheUser> findByEmail(String email);
}
