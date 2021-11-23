package de.apnmt.payment.common.service.stripe.mapper;

import com.stripe.model.Product;
import de.apnmt.payment.common.service.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Service
public class ProductMapper {

    public Map<String, Object> toEntity(ProductDTO dto) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", dto.getName());
        return params;
    }

    public ProductDTO toDto(Product entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public List<ProductDTO> toDto(List<Product> entityList) {
        List<ProductDTO> dtos = new ArrayList<>();
        for (Product product : entityList) {
            ProductDTO dto = toDto(product);
            dtos.add(dto);
        }
        return dtos;
    }
}
