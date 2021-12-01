package org.pyruz.api.shop.repository;

import org.pyruz.api.shop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByTitle(String title);

    List<Product> findProductByCategoryId(Integer categoryId);

    @Query(value = "SELECT p.price, p.title, AVG(r.rate), p.image from product p inner join rate r on r.product_id = p.id " +
            "where (:title = '' or lower(p.title) like %:title%) " +
            "and (:minPrice = 0 or p.price >=:minPrice) " +
            "and (:maxPrice = 0 or p.price <=:maxPrice) " +
            "group By p.id having (Avg(r.rate) between :minRate and :maxRate)", nativeQuery = true)
    List<Object[]> searchProducts(@Param("title") String title,
                                 @Param("minPrice") Float minPrice,
                                 @Param("maxPrice") Float maxPrice,
                                 @Param("minRate") Short minRate,
                                 @Param("maxRate") Short maxRate);

}
