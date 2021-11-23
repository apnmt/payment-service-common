package de.apnmt.payment.common.service.stripe.mapper;

import com.stripe.model.Price;
import de.apnmt.payment.common.domain.enumeration.Currency;
import de.apnmt.payment.common.domain.enumeration.Interval;
import de.apnmt.payment.common.service.dto.PriceDTO;
import de.apnmt.payment.common.service.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for the entity {@link Price} and its DTO {@link PriceDTO}.
 */
@Service
public class PriceMapper {

    public Map<String, Object> toEntity(PriceDTO dto) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", dto.getInterval().toString());
        params.put("nickname", dto.getNickname());
        params.put("currency", dto.getCurrency().toString());
        params.put("recurring", recurring);
        params.put("unit_amount", dto.getAmount());
        params.put("product", dto.getProduct().getId());
        return params;
    }

    public PriceDTO toDto(Price entity) {
        PriceDTO dto = new PriceDTO();
        dto.setId(entity.getId());
        dto.setNickname(entity.getNickname());
        dto.setCurrency(Currency.valueOf(entity.getCurrency()));
        dto.setInterval(Interval.valueOf(entity.getRecurring().getInterval()));
        dto.setAmount(entity.getUnitAmount());
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(entity.getProduct());
        dto.setProduct(productDTO);
        return dto;
    }

    public List<PriceDTO> toDto(List<Price> entityList) {
        List<PriceDTO> dtos = new ArrayList<>();
        for (Price price : entityList) {
            PriceDTO dto = toDto(price);
            dtos.add(dto);
        }
        return dtos;
    }
}
