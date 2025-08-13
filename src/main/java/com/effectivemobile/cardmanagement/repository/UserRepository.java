package com.effectivemobile.cardmanagement.repository;

import com.effectivemobile.cardmanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий пользователей.
 *
 * @author Дмитрий Ельцов
 **/
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    boolean existsByUserName(String userName);

}
