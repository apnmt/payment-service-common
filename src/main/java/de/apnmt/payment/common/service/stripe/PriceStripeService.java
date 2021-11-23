package de.apnmt.payment.common.service.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import de.apnmt.payment.common.service.dto.PriceDTO;
import de.apnmt.payment.common.service.stripe.mapper.PriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PriceStripeService extends StripeService {

    @Autowired
    private PriceMapper priceMapper;

    public PriceDTO save(PriceDTO priceDTO) throws StripeException {
        Map<String, Object> params = this.priceMapper.toEntity(priceDTO);
        Price price = Price.create(params);
        PriceDTO result = this.priceMapper.toDto(price);
        return result;
    }

    public PriceDTO update(PriceDTO priceDTO) throws StripeException {
        Price price = Price.retrieve(priceDTO.getId());
        Map<String, Object> params = new HashMap<>();
        params.put("nickname", priceDTO.getNickname());
        price.update(params);

        PriceDTO result = this.priceMapper.toDto(price);
        return result;
    }
}
