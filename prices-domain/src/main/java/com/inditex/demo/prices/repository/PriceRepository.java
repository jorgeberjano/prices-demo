package com.inditex.demo.prices.repository;


import com.inditex.demo.prices.entity.PriceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;

@Repository
public interface PriceRepository extends CrudRepository<PriceEntity, BigInteger> {
    @Query("SELECT p FROM PriceEntity p " +
            "WHERE p.productId = :productId AND p.brandId = :brandId AND :date BETWEEN p.startDate AND p.endDate")
    Page<PriceEntity> findByProductBrandAndDate(String productId, String brandId, Date date, Pageable pageable);
}
