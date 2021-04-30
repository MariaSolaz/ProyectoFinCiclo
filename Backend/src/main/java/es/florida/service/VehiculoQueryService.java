package es.florida.service;

import es.florida.domain.*; // for static metamodels
import es.florida.domain.Vehiculo;
import es.florida.repository.VehiculoRepository;
import es.florida.service.criteria.VehiculoCriteria;
import es.florida.service.dto.VehiculoDTO;
import es.florida.service.mapper.VehiculoMapper;
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
 * Service for executing complex queries for {@link Vehiculo} entities in the database.
 * The main input is a {@link VehiculoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VehiculoDTO} or a {@link Page} of {@link VehiculoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehiculoQueryService extends QueryService<Vehiculo> {

    private final Logger log = LoggerFactory.getLogger(VehiculoQueryService.class);

    private final VehiculoRepository vehiculoRepository;

    private final VehiculoMapper vehiculoMapper;

    public VehiculoQueryService(VehiculoRepository vehiculoRepository, VehiculoMapper vehiculoMapper) {
        this.vehiculoRepository = vehiculoRepository;
        this.vehiculoMapper = vehiculoMapper;
    }

    /**
     * Return a {@link List} of {@link VehiculoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VehiculoDTO> findByCriteria(VehiculoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Vehiculo> specification = createSpecification(criteria);
        return vehiculoMapper.toDto(vehiculoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VehiculoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VehiculoDTO> findByCriteria(VehiculoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vehiculo> specification = createSpecification(criteria);
        return vehiculoRepository.findAll(specification, page).map(vehiculoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehiculoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vehiculo> specification = createSpecification(criteria);
        return vehiculoRepository.count(specification);
    }

    /**
     * Function to convert {@link VehiculoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vehiculo> createSpecification(VehiculoCriteria criteria) {
        Specification<Vehiculo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vehiculo_.id));
            }
            if (criteria.getMatricula() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatricula(), Vehiculo_.matricula));
            }
            if (criteria.getMarca() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMarca(), Vehiculo_.marca));
            }
            if (criteria.getModelo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelo(), Vehiculo_.modelo));
            }
            if (criteria.getAnyo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnyo(), Vehiculo_.anyo));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Vehiculo_.estado));
            }
            if (criteria.getDuenyoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDuenyoId(), root -> root.join(Vehiculo_.duenyos, JoinType.LEFT).get(Cliente_.id))
                    );
            }
            if (criteria.getMecanicoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMecanicoId(),
                            root -> root.join(Vehiculo_.mecanicos, JoinType.LEFT).get(Mecanico_.id)
                        )
                    );
            }
            if (criteria.getMatriculaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMatriculaId(),
                            root -> root.join(Vehiculo_.matriculas, JoinType.LEFT).get(Factura_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
