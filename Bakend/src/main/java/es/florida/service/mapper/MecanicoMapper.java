package es.florida.service.mapper;

import es.florida.domain.*;
import es.florida.service.dto.MecanicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mecanico} and its DTO {@link MecanicoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MecanicoMapper extends EntityMapper<MecanicoDTO, Mecanico> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MecanicoDTO toDtoId(Mecanico mecanico);
}
