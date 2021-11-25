package de.apnmt.payment.common.repository;

import de.apnmt.payment.common.domain.Price;
import de.apnmt.payment.common.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Price entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PriceRepository extends JpaRepository<Price, String> {

    List<Price> findAllByProduct(Product product);

}
