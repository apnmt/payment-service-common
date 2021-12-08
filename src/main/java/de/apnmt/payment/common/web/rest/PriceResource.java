package de.apnmt.payment.common.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import de.apnmt.common.errors.BadRequestAlertException;
import de.apnmt.payment.common.repository.PriceRepository;
import de.apnmt.payment.common.service.PriceService;
import de.apnmt.payment.common.service.dto.PriceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link PriceDTO}.
 */
@RestController
@RequestMapping("/api/prices")
public class PriceResource {

    private final Logger log = LoggerFactory.getLogger(PriceResource.class);

    private static final String ENTITY_NAME = "payservicePrice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceService priceService;

    private final PriceRepository priceRepository;

    public PriceResource(PriceService priceService, PriceRepository priceRepository) {
        this.priceService = priceService;
        this.priceRepository = priceRepository;
    }

    @PostMapping("")
    public ResponseEntity<PriceDTO> create(@RequestBody PriceDTO priceDTO) throws URISyntaxException {
        this.log.debug("REST request to save Price : {}", priceDTO);
        if (priceDTO.getId() != null) {
            throw new BadRequestAlertException("A new price cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceDTO result = this.priceService.save(priceDTO);
        return ResponseEntity.created(new URI("/api/prices/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(this.applicationName, false, ENTITY_NAME,
                result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceDTO> update(@PathVariable(value = "id", required = false) final String id, @RequestBody PriceDTO priceDTO) {
        this.log.debug("REST request to update Price : {}", priceDTO);
        if (priceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!this.priceRepository.existsById(priceDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        PriceDTO result = this.priceService.update(priceDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, false, ENTITY_NAME, priceDTO.getId())).body(result);
    }

    @GetMapping("/{id}")
    public PriceDTO getPrice(@PathVariable("id") String id) {
        this.log.debug("REST request to get Price : {}", id);
        return this.priceService.findOne(id);
    }

    @GetMapping("/product/{id}")
    public List<PriceDTO> getAllPrices(@PathVariable("id") String id) {
        this.log.debug("REST request to get all Prices for product {}", id);
        return this.priceService.findAllByProduct(id);
    }

}
