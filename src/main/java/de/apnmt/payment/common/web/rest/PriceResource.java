package de.apnmt.payment.common.web.rest;

import de.apnmt.payment.common.service.PriceService;
import de.apnmt.payment.common.service.dto.PriceDTO;
import de.apnmt.payment.common.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

    private PriceService priceService;

    public PriceResource(PriceService priceService) {
        this.priceService = priceService;
    }

    @PostMapping("")
    public ResponseEntity<PriceDTO> create(@RequestBody PriceDTO priceDTO) throws URISyntaxException {
        this.log.debug("REST request to save Price : {}", priceDTO);
        if (priceDTO.getId() != null) {
            throw new BadRequestAlertException("A new price cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceDTO result = this.priceService.save(priceDTO);
        return ResponseEntity.created(new URI("/api/prices/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(this.applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("")
    public ResponseEntity<PriceDTO> update(@RequestBody PriceDTO priceDTO) {
        this.log.debug("REST request to update Price : {}", priceDTO);
        PriceDTO result = this.priceService.update(priceDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, false, ENTITY_NAME, priceDTO.getId().toString()))
                .body(result);
    }

    @GetMapping("/{id}")
    public PriceDTO getPrice(@PathVariable("id") String id) {
        this.log.debug("REST request to get Price : {}", id);
        PriceDTO result = this.priceService.findOne(id);
        return result;
    }

    @GetMapping("/product/{id}")
    public List<PriceDTO> getAllPrices(@PathVariable("id") String id) {
        this.log.debug("REST request to get all Prices for price {}", id);
        List<PriceDTO> result = this.priceService.findAllByProduct(id);
        return result;
    }

}
