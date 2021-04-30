package es.florida.service.mapper;

import es.florida.domain.*;
import es.florida.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring", uses = { VehiculoMapper.class })
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "vehiculo", source = "vehiculo", qualifiedByName = "id")
    ClienteDTO toDto(Cliente s);
}
