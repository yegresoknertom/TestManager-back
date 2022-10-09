package org.testmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.testmanager.model.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
