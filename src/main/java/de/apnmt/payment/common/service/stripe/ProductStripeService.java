package de.apnmt.payment.common.service.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import de.apnmt.payment.common.service.dto.ProductDTO;
import de.apnmt.payment.common.service.stripe.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductStripeService extends StripeService {

    @Autowired
    private ProductMapper productMapper;

    public ProductDTO save(ProductDTO productDTO) throws StripeException {
        Map<String, Object> params = this.productMapper.toEntity(productDTO);
        Product product = Product.create(params);

        ProductDTO result = this.productMapper.toDto(product);
        return result;
    }

    public ProductDTO update(ProductDTO productDTO) throws StripeException {
        Product product = Product.retrieve(productDTO.getId());
        Map<String, Object> params = this.productMapper.toEntity(productDTO);
        product = product.update(params);

        ProductDTO result = this.productMapper.toDto(product);
        return result;
    }

}
