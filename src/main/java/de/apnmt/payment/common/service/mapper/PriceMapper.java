package de.apnmt.payment.common.service.mapper;

import de.apnmt.payment.common.domain.Price;
import de.apnmt.payment.common.service.dto.PriceDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Price} and its DTO {@link PriceDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface PriceMapper extends EntityMapper<PriceDTO, Price> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    PriceDTO toDto(Price s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PriceDTO toDtoId(Price price);
}
