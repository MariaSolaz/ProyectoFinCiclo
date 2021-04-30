package es.florida.service.mapper;

import es.florida.domain.*;
import es.florida.service.dto.VehiculoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vehiculo} and its DTO {@link VehiculoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VehiculoMapper extends EntityMapper<VehiculoDTO, Vehiculo> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehiculoDTO toDtoId(Vehiculo vehiculo);
}
