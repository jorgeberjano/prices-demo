package com.inditex.demo.prices.repository;


import com.inditex.demo.prices.entity.PriceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends CrudRepository<PriceEntity, BigInteger> {

    @Query("SELECT p FROM PriceEntity p " +
            "WHERE p.productId = :productId AND p.brandId = :brandId AND :date BETWEEN p.startDate AND p.endDate " +
            "ORDER BY p.priority DESC")
    List<PriceEntity> findByProductBrandAndDate(String productId, String brandId, LocalDateTime date);

    default Optional<PriceEntity> findFirstByProductBrandAndDate(String productId, String brandId, LocalDateTime date) {
        return findByProductBrandAndDate(productId, brandId, date).stream().findFirst();
    }
}
