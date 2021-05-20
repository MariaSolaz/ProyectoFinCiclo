package es.florida.service.mapper;

import es.florida.domain.*;
import es.florida.service.dto.VehiculoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vehiculo} and its DTO {@link VehiculoDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClienteMapper.class, MecanicoMapper.class })
public interface VehiculoMapper extends EntityMapper<VehiculoDTO, Vehiculo> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    @Mapping(target = "mecanico", source = "mecanico", qualifiedByName = "id")
    VehiculoDTO toDto(Vehiculo s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehiculoDTO toDtoId(Vehiculo vehiculo);
}
