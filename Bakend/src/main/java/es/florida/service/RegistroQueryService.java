package es.florida.service;

import es.florida.domain.*; // for static metamodels
import es.florida.domain.Registro;
import es.florida.repository.RegistroRepository;
import es.florida.service.criteria.RegistroCriteria;
import es.florida.service.dto.RegistroDTO;
import es.florida.service.mapper.RegistroMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Registro} entities in the database.
 * The main input is a {@link RegistroCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RegistroDTO} or a {@link Page} of {@link RegistroDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegistroQueryService extends QueryService<Registro> {

    private final Logger log = LoggerFactory.getLogger(RegistroQueryService.class);

    private final RegistroRepository registroRepository;

    private final RegistroMapper registroMapper;

    public RegistroQueryService(RegistroRepository registroRepository, RegistroMapper registroMapper) {
        this.registroRepository = registroRepository;
        this.registroMapper = registroMapper;
    }

    /**
     * Return a {@link List} of {@link RegistroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegistroDTO> findByCriteria(RegistroCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Registro> specification = createSpecification(criteria);
        return registroMapper.toDto(registroRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RegistroDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RegistroDTO> findByCriteria(RegistroCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Registro> specification = createSpecification(criteria);
        return registroRepository.findAll(specification, page).map(registroMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegistroCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Registro> specification = createSpecification(criteria);
        return registroRepository.count(specification);
    }

    /**
     * Function to convert {@link RegistroCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Registro> createSpecification(RegistroCriteria criteria) {
        Specification<Registro> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Registro_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Registro_.fecha));
            }
            if (criteria.getEstadoActual() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstadoActual(), Registro_.estadoActual));
            }
            if (criteria.getVehiculoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVehiculoId(), root -> root.join(Registro_.vehiculo, JoinType.LEFT).get(Vehiculo_.id))
                    );
            }
        }
        return specification;
    }
}
