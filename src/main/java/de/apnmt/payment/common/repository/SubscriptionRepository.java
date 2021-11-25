package de.apnmt.payment.common.repository;

import de.apnmt.payment.common.domain.Customer;
import de.apnmt.payment.common.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Subscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    List<Subscription> findAllByCustomer(Customer customer);

}
