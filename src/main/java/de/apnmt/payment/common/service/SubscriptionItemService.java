package de.apnmt.payment.common.service;

import de.apnmt.payment.common.domain.SubscriptionItem;
import de.apnmt.payment.common.repository.SubscriptionItemRepository;
import de.apnmt.payment.common.service.dto.SubscriptionItemDTO;
import de.apnmt.payment.common.service.mapper.SubscriptionItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SubscriptionItem}.
 */
@Service
@Transactional
public class SubscriptionItemService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionItemService.class);

    private final SubscriptionItemRepository subscriptionItemRepository;

    private final SubscriptionItemMapper subscriptionItemMapper;

    public SubscriptionItemService(SubscriptionItemRepository subscriptionItemRepository, SubscriptionItemMapper subscriptionItemMapper) {
        this.subscriptionItemRepository = subscriptionItemRepository;
        this.subscriptionItemMapper = subscriptionItemMapper;
    }

    /**
     * Save a subscriptionItem.
     *
     * @param subscriptionItemDTO the entity to save.
     * @return the persisted entity.
     */
    public SubscriptionItemDTO save(SubscriptionItemDTO subscriptionItemDTO) {
        log.debug("Request to save SubscriptionItem : {}", subscriptionItemDTO);
        SubscriptionItem subscriptionItem = subscriptionItemMapper.toEntity(subscriptionItemDTO);
        subscriptionItem = subscriptionItemRepository.save(subscriptionItem);
        return subscriptionItemMapper.toDto(subscriptionItem);
    }

    /**
     * Partially update a subscriptionItem.
     *
     * @param subscriptionItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubscriptionItemDTO> partialUpdate(SubscriptionItemDTO subscriptionItemDTO) {
        log.debug("Request to partially update SubscriptionItem : {}", subscriptionItemDTO);

        return subscriptionItemRepository
            .findById(subscriptionItemDTO.getId())
            .map(
                existingSubscriptionItem -> {
                    subscriptionItemMapper.partialUpdate(existingSubscriptionItem, subscriptionItemDTO);

                    return existingSubscriptionItem;
                }
            )
            .map(subscriptionItemRepository::save)
            .map(subscriptionItemMapper::toDto);
    }

    /**
     * Get all the subscriptionItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubscriptionItemDTO> findAll() {
        log.debug("Request to get all SubscriptionItems");
        return subscriptionItemRepository
            .findAll()
            .stream()
            .map(subscriptionItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one subscriptionItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubscriptionItemDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionItem : {}", id);
        return subscriptionItemRepository.findById(id).map(subscriptionItemMapper::toDto);
    }

    /**
     * Delete the subscriptionItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionItem : {}", id);
        subscriptionItemRepository.deleteById(id);
    }
}
