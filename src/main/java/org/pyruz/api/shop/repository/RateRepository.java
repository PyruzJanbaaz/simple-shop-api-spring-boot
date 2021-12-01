package org.pyruz.api.shop.repository;

import org.pyruz.api.shop.model.entity.Rate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends CrudRepository<Rate, Integer> {

    Rate findByProductIdAndUserId(Integer productId, Integer userId);

    List<Rate> findAllByProductId(Integer productId);
}
