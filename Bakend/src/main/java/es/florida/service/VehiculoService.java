package es.florida.service;

import es.florida.service.dto.VehiculoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link es.florida.domain.Vehiculo}.
 */
public interface VehiculoService {
    /**
     * Save a vehiculo.
     *
     * @param vehiculoDTO the entity to save.
     * @return the persisted entity.
     */
    VehiculoDTO save(VehiculoDTO vehiculoDTO);

    /**
     * Partially updates a vehiculo.
     *
     * @param vehiculoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehiculoDTO> partialUpdate(VehiculoDTO vehiculoDTO);

    /**
     * Get all the vehiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehiculoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vehiculo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehiculoDTO> findOne(Long id);

    /**
     * Delete the "id" vehiculo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
