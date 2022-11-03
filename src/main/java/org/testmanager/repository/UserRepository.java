package org.testmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.testmanager.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select * from users where locked is false limit 1", nativeQuery = true)
    Optional<User> findFreeUser();
    Optional<User> findByLogin(String login);

}
