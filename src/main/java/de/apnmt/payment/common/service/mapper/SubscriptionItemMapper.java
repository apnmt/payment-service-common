package de.apnmt.payment.common.service.mapper;

import de.apnmt.payment.common.domain.SubscriptionItem;
import de.apnmt.payment.common.service.dto.SubscriptionItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link SubscriptionItem} and its DTO {@link SubscriptionItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { PriceMapper.class, SubscriptionMapper.class })
public interface SubscriptionItemMapper extends EntityMapper<SubscriptionItemDTO, SubscriptionItem> {
    @Mapping(target = "price", source = "price", qualifiedByName = "id")
    @Mapping(target = "subscription", source = "subscription", qualifiedByName = "id")
    SubscriptionItemDTO toDto(SubscriptionItem s);
}
