package es.florida.service;

import es.florida.service.dto.MecanicoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link es.florida.domain.Mecanico}.
 */
public interface MecanicoService {
    /**
     * Save a mecanico.
     *
     * @param mecanicoDTO the entity to save.
     * @return the persisted entity.
     */
    MecanicoDTO save(MecanicoDTO mecanicoDTO);

    /**
     * Partially updates a mecanico.
     *
     * @param mecanicoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MecanicoDTO> partialUpdate(MecanicoDTO mecanicoDTO);

    /**
     * Get all the mecanicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MecanicoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mecanico.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MecanicoDTO> findOne(Long id);

    /**
     * Delete the "id" mecanico.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
