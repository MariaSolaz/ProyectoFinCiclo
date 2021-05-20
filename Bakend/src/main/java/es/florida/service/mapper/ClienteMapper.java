package es.florida.service.mapper;

import es.florida.domain.*;
import es.florida.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    ClienteDTO toDto(Cliente s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user")
    ClienteDTO toDtoId(Cliente cliente);
}
