package de.apnmt.payment.common.repository;

import de.apnmt.payment.common.domain.SubscriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubscriptionItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionItemRepository extends JpaRepository<SubscriptionItem, Long> {}
