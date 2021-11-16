package de.apnmt.payment.common.service;

import de.apnmt.payment.common.domain.Price;
import de.apnmt.payment.common.repository.PriceRepository;
import de.apnmt.payment.common.service.dto.PriceDTO;
import de.apnmt.payment.common.service.mapper.PriceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Price}.
 */
@Service
@Transactional
public class PriceService {

    private final Logger log = LoggerFactory.getLogger(PriceService.class);

    private final PriceRepository priceRepository;

    private final PriceMapper priceMapper;

    public PriceService(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    /**
     * Save a price.
     *
     * @param priceDTO the entity to save.
     * @return the persisted entity.
     */
    public PriceDTO save(PriceDTO priceDTO) {
        log.debug("Request to save Price : {}", priceDTO);
        Price price = priceMapper.toEntity(priceDTO);
        price = priceRepository.save(price);
        return priceMapper.toDto(price);
    }

    /**
     * Partially update a price.
     *
     * @param priceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PriceDTO> partialUpdate(PriceDTO priceDTO) {
        log.debug("Request to partially update Price : {}", priceDTO);

        return priceRepository
            .findById(priceDTO.getId())
            .map(
                existingPrice -> {
                    priceMapper.partialUpdate(existingPrice, priceDTO);

                    return existingPrice;
                }
            )
            .map(priceRepository::save)
            .map(priceMapper::toDto);
    }

    /**
     * Get all the prices.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PriceDTO> findAll() {
        log.debug("Request to get all Prices");
        return priceRepository.findAll().stream().map(priceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one price by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PriceDTO> findOne(Long id) {
        log.debug("Request to get Price : {}", id);
        return priceRepository.findById(id).map(priceMapper::toDto);
    }

    /**
     * Delete the price by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Price : {}", id);
        priceRepository.deleteById(id);
    }
}
