package de.apnmt.payment.common.service.mapper;

import de.apnmt.payment.common.domain.Subscription;
import de.apnmt.payment.common.service.dto.SubscriptionDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Subscription} and its DTO {@link SubscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public interface SubscriptionMapper extends EntityMapper<SubscriptionDTO, Subscription> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    SubscriptionDTO toDto(Subscription s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubscriptionDTO toDtoId(Subscription subscription);
}
