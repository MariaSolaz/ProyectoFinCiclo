package es.florida.service.mapper;

import es.florida.domain.*;
import es.florida.service.dto.FacturaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factura} and its DTO {@link FacturaDTO}.
 */
@Mapper(componentModel = "spring", uses = { VehiculoMapper.class })
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {
    @Mapping(target = "vehiculo", source = "vehiculo", qualifiedByName = "id")
    FacturaDTO toDto(Factura s);
}
