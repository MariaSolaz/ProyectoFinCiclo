package es.florida.service.mapper;

import es.florida.domain.*;
import es.florida.service.dto.RegistroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Registro} and its DTO {@link RegistroDTO}.
 */
@Mapper(componentModel = "spring", uses = { VehiculoMapper.class })
public interface RegistroMapper extends EntityMapper<RegistroDTO, Registro> {
    @Mapping(target = "vehiculo", source = "vehiculo", qualifiedByName = "id")
    RegistroDTO toDto(Registro s);
}
