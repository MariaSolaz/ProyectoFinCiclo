package es.florida.service;

import es.florida.domain.*; // for static metamodels
import es.florida.domain.Factura;
import es.florida.repository.FacturaRepository;
import es.florida.service.criteria.FacturaCriteria;
import es.florida.service.dto.FacturaDTO;
import es.florida.service.mapper.FacturaMapper;
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
 * Service for executing complex queries for {@link Factura} entities in the database.
 * The main input is a {@link FacturaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FacturaDTO} or a {@link Page} of {@link FacturaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FacturaQueryService extends QueryService<Factura> {

    private final Logger log = LoggerFactory.getLogger(FacturaQueryService.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    public FacturaQueryService(FacturaRepository facturaRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
    }

    /**
     * Return a {@link List} of {@link FacturaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FacturaDTO> findByCriteria(FacturaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaMapper.toDto(facturaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FacturaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FacturaDTO> findByCriteria(FacturaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.findAll(specification, page).map(facturaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FacturaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Factura> specification = createSpecification(criteria);
        return facturaRepository.count(specification);
    }

    /**
     * Function to convert {@link FacturaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Factura> createSpecification(FacturaCriteria criteria) {
        Specification<Factura> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Factura_.id));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Factura_.fecha));
            }
            if (criteria.getPrecio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecio(), Factura_.precio));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Factura_.estado));
            }
            if (criteria.getVehiculoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVehiculoId(), root -> root.join(Factura_.vehiculo, JoinType.LEFT).get(Vehiculo_.id))
                    );
            }
        }
        return specification;
    }
}
