package de.apnmt.payment.common.service;

import com.stripe.exception.StripeException;
import de.apnmt.common.errors.BadRequestAlertException;
import de.apnmt.payment.common.domain.Price;
import de.apnmt.payment.common.domain.Product;
import de.apnmt.payment.common.repository.PriceRepository;
import de.apnmt.payment.common.repository.ProductRepository;
import de.apnmt.payment.common.service.dto.PriceDTO;
import de.apnmt.payment.common.service.errors.PriceNotFoundException;
import de.apnmt.payment.common.service.errors.ProductNotFoundException;
import de.apnmt.payment.common.service.mapper.PriceMapper;
import de.apnmt.payment.common.service.stripe.PriceStripeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    private final PriceRepository priceRepository;
    private final PriceStripeService priceStripeService;
    private final PriceMapper priceMapper;
    private final ProductRepository productRepository;

    public PriceService(PriceRepository priceRepository, PriceStripeService priceStripeService, PriceMapper priceMapper, ProductRepository productRepository) {
        this.priceStripeService = priceStripeService;
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
        this.productRepository = productRepository;
    }


    public PriceDTO save(PriceDTO priceDTO) {
        try {
            Optional<Product> optional = this.productRepository.findById(priceDTO.getProduct().getId());
            if (optional.isEmpty()) {
                throw new ProductNotFoundException(priceDTO.getProduct().getId());
            }
            PriceDTO stripeResult = this.priceStripeService.save(priceDTO);
            Price price = this.priceMapper.toEntity(priceDTO);
            price.setId(stripeResult.getId());
            Product product = optional.get();
            product.addPrice(price);
            price = this.priceRepository.save(price);
            this.productRepository.save(product);
            return this.priceMapper.toDto(price);
        } catch (StripeException ex) {
            throw new BadRequestAlertException(ex.getMessage(), "Stripe", ex.getCode());
        }
    }

    public PriceDTO update(PriceDTO priceDTO) {
        try {
            this.priceStripeService.update(priceDTO);
            Price price = this.priceMapper.toEntity(priceDTO);
            price = this.priceRepository.save(price);
            return this.priceMapper.toDto(price);
        } catch (StripeException ex) {
            throw new BadRequestAlertException(ex.getMessage(), "Stripe", ex.getCode());
        }
    }

    public PriceDTO findOne(String id) {
        Optional<Price> price = this.priceRepository.findById(id);
        if (price.isEmpty()) {
            throw new PriceNotFoundException(id);
        }
        return this.priceMapper.toDto(price.get());
    }

    public Price validatePrice(Price price) {
        Optional<Price> maybe = this.priceRepository.findById(price.getId());
        if (maybe.isEmpty()) {
            throw new PriceNotFoundException(price.getId());
        } else if (maybe.get().getProduct() == null) {
            throw new ProductNotFoundException(null);
        }
        return maybe.get();
    }

    public List<PriceDTO> findAllByProduct(String productId) {
        Optional<Product> product = this.productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }
        List<Price> prices = this.priceRepository.findAllByProduct(product.get());
        List<PriceDTO> priceDTOS = this.priceMapper.toDto(prices);
        return priceDTOS;
    }

    /**
     * Delete all prices.
     */
    public void deleteAll() {
        Optional<Product> product = this.productRepository.findById("prod_LQ9MM3UEDGiaJg");
        product.ifPresent(priceRepository::deleteAllByProductIsNot);
    }

}
