package de.apnmt.payment.common.web.rest;

import de.apnmt.payment.common.service.ProductService;
import de.apnmt.payment.common.service.dto.ProductDTO;
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
 * REST controller for managing {@link ProductDTO}.
 */
@RestController
@RequestMapping("/api/products")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "payserviceProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        this.log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductDTO result = this.productService.save(productDTO);
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(this.applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("")
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        this.log.debug("REST request to update Product : {}", productDTO);
        ProductDTO result = this.productService.update(productDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, false, ENTITY_NAME, productDTO.getId().toString()))
                .body(result);
    }

    @GetMapping("")
    public List<ProductDTO> getAllProducts() {
        this.log.debug("REST request to get all Products");
        List<ProductDTO> result = this.productService.findAll();
        return result;
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable("id") String id) {
        this.log.debug("REST request to get Product : {}", id);
        ProductDTO result = this.productService.findOne(id);
        return result;
    }

}
