package de.apnmt.payment.common.service;

import de.apnmt.payment.common.domain.Subscription;
import de.apnmt.payment.common.repository.SubscriptionRepository;
import de.apnmt.payment.common.service.dto.SubscriptionDTO;
import de.apnmt.payment.common.service.mapper.SubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Subscription}.
 */
@Service
@Transactional
public class SubscriptionService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, SubscriptionMapper subscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionMapper = subscriptionMapper;
    }

    /**
     * Save a Subscription.
     *
     * @param subscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionDTO save(SubscriptionDTO subscriptionDTO) {
        log.debug("Request to save Subscription : {}", subscriptionDTO);
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        subscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(subscription);
    }

    /**
     * Partially update a Subscription.
     *
     * @param subscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscriptionDTO> partialUpdate(SubscriptionDTO subscriptionDTO) {
        log.debug("Request to partially update Subscription : {}", subscriptionDTO);

        return subscriptionRepository
            .findById(subscriptionDTO.getId())
            .map(
                existingSubscription -> {
                    subscriptionMapper.partialUpdate(existingSubscription, subscriptionDTO);

                    return existingSubscription;
                }
            )
            .map(subscriptionRepository::save)
            .map(subscriptionMapper::toDto);
    }

    /**
     * Get all the Subscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Subscriptions");
        return subscriptionRepository.findAll(pageable).map(subscriptionMapper::toDto);
    }

    /**
     * Get one Subscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionDTO> findOne(Long id) {
        log.debug("Request to get Subscription : {}", id);
        return subscriptionRepository.findById(id).map(subscriptionMapper::toDto);
    }

    /**
     * Delete the Subscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Subscription : {}", id);
        subscriptionRepository.deleteById(id);
    }
}
