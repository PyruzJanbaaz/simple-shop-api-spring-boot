package org.pyruz.api.shop.repository;

import org.pyruz.api.shop.model.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    Optional<Category> findByTitle(String title);

}
