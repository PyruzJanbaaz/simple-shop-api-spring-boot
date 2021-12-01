package org.pyruz.api.shop.repository;

import org.pyruz.api.shop.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    Page<Role> findAll(Pageable pageable);

    Role findRoleByTitleIgnoreCase(String title);

}
