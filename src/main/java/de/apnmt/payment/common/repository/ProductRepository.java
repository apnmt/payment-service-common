package de.apnmt.payment.common.repository;

import de.apnmt.payment.common.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    void deleteAllByIdIsNot(String id);

}
