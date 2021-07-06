package com.inditex.demo.prices.repository;


import com.inditex.demo.prices.entity.PriceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceReactiveRepository extends ReactiveCrudRepository<PriceEntity, BigInteger> {

    @Query("SELECT p FROM PriceEntity p " +
            "WHERE p.productId = :productId AND p.brandId = :brandId AND :date BETWEEN p.startDate AND p.endDate " +
            "ORDER BY p.priority DESC")
    Flux<PriceEntity> findByProductBrandAndDate(String productId, String brandId, LocalDateTime date);

    default Mono<PriceEntity> findFirstByProductBrandAndDate(String productId, String brandId, LocalDateTime date) {
        return findByProductBrandAndDate(productId, brandId, date).take(1).single();
    }
}
