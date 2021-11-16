package de.apnmt.payment.common.web.rest;

import de.apnmt.payment.common.domain.Subscription;
import de.apnmt.payment.common.repository.SubscriptionRepository;
import de.apnmt.payment.common.service.SubscriptionService;
import de.apnmt.payment.common.service.dto.SubscriptionDTO;
import de.apnmt.payment.common.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Subscription}.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionResource.class);

    private static final String ENTITY_NAME = "paymentServiceSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionService subscriptionService;

    private final SubscriptionRepository SubscriptionRepository;

    public SubscriptionResource(SubscriptionService subscriptionService, SubscriptionRepository SubscriptionRepository) {
        this.subscriptionService = subscriptionService;
        this.SubscriptionRepository = SubscriptionRepository;
    }

    /**
     * {@code POST  /subscriptions} : Create a new Subscription.
     *
     * @param subscriptionDTO the SubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new SubscriptionDTO, or with status {@code 400 (Bad Request)} if the Subscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionDTO> createSubscription(@Valid @RequestBody SubscriptionDTO subscriptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save Subscription : {}", subscriptionDTO);
        if (subscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new Subscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionDTO result = subscriptionService.save(subscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/t-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /t-subscriptions/:id} : Updates an existing Subscription.
     *
     * @param id the id of the SubscriptionDTO to save.
     * @param subscriptionDTO the SubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated SubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the SubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the SubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/t-subscriptions/{id}")
    public ResponseEntity<SubscriptionDTO> updateSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionDTO subscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Subscription : {}, {}", id, subscriptionDTO);
        if (subscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!SubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubscriptionDTO result = subscriptionService.save(subscriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /t-subscriptions/:id} : Partial updates given fields of an existing Subscription, field will ignore if it is null
     *
     * @param id the id of the SubscriptionDTO to save.
     * @param subscriptionDTO the SubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated SubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the SubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the SubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the SubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/t-subscriptions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SubscriptionDTO> partialUpdateSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionDTO subscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Subscription partially : {}, {}", id, subscriptionDTO);
        if (subscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!SubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionDTO> result = subscriptionService.partialUpdate(subscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /t-subscriptions} : get all the Subscriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Subscriptions in body.
     */
    @GetMapping("/t-subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions(Pageable pageable) {
        log.debug("REST request to get a page of Subscriptions");
        Page<SubscriptionDTO> page = subscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /t-subscriptions/:id} : get the "id" Subscription.
     *
     * @param id the id of the SubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the SubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/t-subscriptions/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscription(@PathVariable Long id) {
        log.debug("REST request to get Subscription : {}", id);
        Optional<SubscriptionDTO> SubscriptionDTO = subscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(SubscriptionDTO);
    }

    /**
     * {@code DELETE  /t-subscriptions/:id} : delete the "id" Subscription.
     *
     * @param id the id of the SubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/t-subscriptions/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        log.debug("REST request to delete Subscription : {}", id);
        subscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
