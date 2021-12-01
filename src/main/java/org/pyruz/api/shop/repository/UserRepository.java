package org.pyruz.api.shop.repository;

import org.pyruz.api.shop.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByUsernameIgnoreCase(String username);

    Page<User> findAll(Pageable pageRequest);
}
