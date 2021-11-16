package de.apnmt.payment.common.web.rest;

import de.apnmt.payment.common.domain.SubscriptionItem;
import de.apnmt.payment.common.repository.SubscriptionItemRepository;
import de.apnmt.payment.common.service.SubscriptionItemService;
import de.apnmt.payment.common.service.dto.SubscriptionItemDTO;
import de.apnmt.payment.common.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link SubscriptionItem}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionItemResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionItemResource.class);

    private static final String ENTITY_NAME = "paymentServiceSubscriptionItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionItemService subscriptionItemService;

    private final SubscriptionItemRepository subscriptionItemRepository;

    public SubscriptionItemResource(
        SubscriptionItemService subscriptionItemService,
        SubscriptionItemRepository subscriptionItemRepository
    ) {
        this.subscriptionItemService = subscriptionItemService;
        this.subscriptionItemRepository = subscriptionItemRepository;
    }

    /**
     * {@code POST  /subscription-items} : Create a new subscriptionItem.
     *
     * @param subscriptionItemDTO the subscriptionItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionItemDTO, or with status {@code 400 (Bad Request)} if the subscriptionItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscription-items")
    public ResponseEntity<SubscriptionItemDTO> createSubscriptionItem(@Valid @RequestBody SubscriptionItemDTO subscriptionItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save SubscriptionItem : {}", subscriptionItemDTO);
        if (subscriptionItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionItemDTO result = subscriptionItemService.save(subscriptionItemDTO);
        return ResponseEntity
            .created(new URI("/api/subscription-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-items/:id} : Updates an existing subscriptionItem.
     *
     * @param id the id of the subscriptionItemDTO to save.
     * @param subscriptionItemDTO the subscriptionItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionItemDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subscription-items/{id}")
    public ResponseEntity<SubscriptionItemDTO> updateSubscriptionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionItemDTO subscriptionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionItem : {}, {}", id, subscriptionItemDTO);
        if (subscriptionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubscriptionItemDTO result = subscriptionItemService.save(subscriptionItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subscription-items/:id} : Partial updates given fields of an existing subscriptionItem, field will ignore if it is null
     *
     * @param id the id of the subscriptionItemDTO to save.
     * @param subscriptionItemDTO the subscriptionItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionItemDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subscription-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SubscriptionItemDTO> partialUpdateSubscriptionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionItemDTO subscriptionItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionItem partially : {}, {}", id, subscriptionItemDTO);
        if (subscriptionItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionItemDTO> result = subscriptionItemService.partialUpdate(subscriptionItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-items} : get all the subscriptionItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionItems in body.
     */
    @GetMapping("/subscription-items")
    public List<SubscriptionItemDTO> getAllSubscriptionItems() {
        log.debug("REST request to get all SubscriptionItems");
        return subscriptionItemService.findAll();
    }

    /**
     * {@code GET  /subscription-items/:id} : get the "id" subscriptionItem.
     *
     * @param id the id of the subscriptionItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subscription-items/{id}")
    public ResponseEntity<SubscriptionItemDTO> geSubscriptionItem(@PathVariable Long id) {
        log.debug("REST request to get SubscriptionItem : {}", id);
        Optional<SubscriptionItemDTO> subscriptionItemDTO = subscriptionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionItemDTO);
    }

    /**
     * {@code DELETE  /subscription-items/:id} : delete the "id" subscriptionItem.
     *
     * @param id the id of the subscriptionItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subscription-items/{id}")
    public ResponseEntity<Void> deleteSubscriptionItem(@PathVariable Long id) {
        log.debug("REST request to delete SubscriptionItem : {}", id);
        subscriptionItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
