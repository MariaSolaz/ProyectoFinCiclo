package es.florida.service.mapper;

import es.florida.domain.*;
import es.florida.service.dto.MecanicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mecanico} and its DTO {@link MecanicoDTO}.
 */
@Mapper(componentModel = "spring", uses = { VehiculoMapper.class })
public interface MecanicoMapper extends EntityMapper<MecanicoDTO, Mecanico> {
    @Mapping(target = "vehiculo", source = "vehiculo", qualifiedByName = "id")
    MecanicoDTO toDto(Mecanico s);
}
