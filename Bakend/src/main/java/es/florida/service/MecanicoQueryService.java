package es.florida.service;

import es.florida.domain.*; // for static metamodels
import es.florida.domain.Mecanico;
import es.florida.repository.MecanicoRepository;
import es.florida.service.criteria.MecanicoCriteria;
import es.florida.service.dto.MecanicoDTO;
import es.florida.service.mapper.MecanicoMapper;
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
 * Service for executing complex queries for {@link Mecanico} entities in the database.
 * The main input is a {@link MecanicoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MecanicoDTO} or a {@link Page} of {@link MecanicoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MecanicoQueryService extends QueryService<Mecanico> {

    private final Logger log = LoggerFactory.getLogger(MecanicoQueryService.class);

    private final MecanicoRepository mecanicoRepository;

    private final MecanicoMapper mecanicoMapper;

    public MecanicoQueryService(MecanicoRepository mecanicoRepository, MecanicoMapper mecanicoMapper) {
        this.mecanicoRepository = mecanicoRepository;
        this.mecanicoMapper = mecanicoMapper;
    }

    /**
     * Return a {@link List} of {@link MecanicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MecanicoDTO> findByCriteria(MecanicoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mecanico> specification = createSpecification(criteria);
        return mecanicoMapper.toDto(mecanicoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MecanicoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MecanicoDTO> findByCriteria(MecanicoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mecanico> specification = createSpecification(criteria);
        return mecanicoRepository.findAll(specification, page).map(mecanicoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MecanicoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mecanico> specification = createSpecification(criteria);
        return mecanicoRepository.count(specification);
    }

    /**
     * Function to convert {@link MecanicoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mecanico> createSpecification(MecanicoCriteria criteria) {
        Specification<Mecanico> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mecanico_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Mecanico_.nombre));
            }
            if (criteria.getApellido() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApellido(), Mecanico_.apellido));
            }
            if (criteria.getdNI() != null) {
                specification = specification.and(buildStringSpecification(criteria.getdNI(), Mecanico_.dNI));
            }
            if (criteria.getTelefono() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefono(), Mecanico_.telefono));
            }
            if (criteria.getCorreo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCorreo(), Mecanico_.correo));
            }
            if (criteria.getMecanicoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMecanicoId(),
                            root -> root.join(Mecanico_.mecanicos, JoinType.LEFT).get(Vehiculo_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
