package de.apnmt.payment.common.service;

import com.stripe.exception.StripeException;
import de.apnmt.payment.common.domain.Product;
import de.apnmt.payment.common.repository.ProductRepository;
import de.apnmt.payment.common.service.dto.ProductDTO;
import de.apnmt.payment.common.service.errors.ProductNotFoundException;
import de.apnmt.payment.common.service.mapper.ProductMapper;
import de.apnmt.payment.common.service.stripe.ProductStripeService;
import de.apnmt.payment.common.web.rest.errors.BadRequestAlertException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductStripeService productStripeService;
    private ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductStripeService productStripeService, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productStripeService = productStripeService;
        this.productMapper = productMapper;
    }


    public ProductDTO save(ProductDTO productDTO) {
        try {
            ProductDTO stripeResult = this.productStripeService.save(productDTO);
            Product product = this.productMapper.toEntity(productDTO);
            product.setId(stripeResult.getId());
            product = this.productRepository.save(product);
            ProductDTO result = this.productMapper.toDto(product);
            return result;
        } catch (StripeException ex) {
            throw new BadRequestAlertException(ex.getMessage(), "Stripe", ex.getCode());
        }
    }

    public ProductDTO update(ProductDTO productDTO) {
        try {
            this.productStripeService.update(productDTO);
            Product product = this.productMapper.toEntity(productDTO);
            product = this.productRepository.save(product);
            ProductDTO result = this.productMapper.toDto(product);
            return result;
        } catch (StripeException ex) {
            throw new BadRequestAlertException(ex.getMessage(), "Stripe", ex.getCode());
        }
    }

    public ProductDTO findOne(String id) {
        Optional<Product> maybe = this.productRepository.findById(id);
        if (maybe.isEmpty()) {
            throw new ProductNotFoundException();
        }
        Product product = maybe.get();
        return this.productMapper.toDto(product);
    }

    public List<ProductDTO> findAll() {
        List<Product> products = this.productRepository.findAll();
        List<ProductDTO> productDTOS = this.productMapper.toDto(products);
        return productDTOS;
    }

}
