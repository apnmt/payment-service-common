package de.apnmt.payment.common.web.rest;

import de.apnmt.common.errors.BadRequestAlertException;
import de.apnmt.payment.common.repository.ProductRepository;
import de.apnmt.payment.common.service.ProductService;
import de.apnmt.payment.common.service.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

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

    private final ProductService productService;

    private final ProductRepository productRepository;

    public ProductResource(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        this.log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductDTO result = this.productService.save(productDTO);
        return ResponseEntity.created(new URI("/api/products/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(this.applicationName, false, ENTITY_NAME,
                result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable(value = "id", required = false) final String id, @RequestBody ProductDTO productDTO) {
        this.log.debug("REST request to update Product : {}", productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!this.productRepository.existsById(productDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        ProductDTO result = this.productService.update(productDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, false, ENTITY_NAME, productDTO.getId())).body(result);
    }

    @GetMapping("")
    public List<ProductDTO> getAllProducts() {
        this.log.debug("REST request to get all Products");
        return this.productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable("id") String id) {
        this.log.debug("REST request to get Product : {}", id);
        return this.productService.findOne(id);
    }

    /**
     * {@code DELETE  /products} : delete all products.
     *
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("")
    public ResponseEntity<Void> deleteAll() {
        this.log.debug("REST request to delete all Products");
        this.productService.deleteAll();
        return ResponseEntity
                .noContent()
                .build();
    }

}
