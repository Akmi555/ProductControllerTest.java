package de.ait_tr.shop.repository;

import de.ait_tr.shop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findUserByUserName(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}

/*
findBy - объект, списки, страницы, поток и Optional
countBy - long
deleteBy - void или объект
existsBy - boolean
 */
