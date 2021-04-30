package es.florida.web.rest;

import es.florida.repository.VehiculoRepository;
import es.florida.service.VehiculoQueryService;
import es.florida.service.VehiculoService;
import es.florida.service.criteria.VehiculoCriteria;
import es.florida.service.dto.VehiculoDTO;
import es.florida.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link es.florida.domain.Vehiculo}.
 */
@RestController
@RequestMapping("/api")
public class VehiculoResource {

    private final Logger log = LoggerFactory.getLogger(VehiculoResource.class);

    private static final String ENTITY_NAME = "vehiculo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehiculoService vehiculoService;

    private final VehiculoRepository vehiculoRepository;

    private final VehiculoQueryService vehiculoQueryService;

    public VehiculoResource(
        VehiculoService vehiculoService,
        VehiculoRepository vehiculoRepository,
        VehiculoQueryService vehiculoQueryService
    ) {
        this.vehiculoService = vehiculoService;
        this.vehiculoRepository = vehiculoRepository;
        this.vehiculoQueryService = vehiculoQueryService;
    }

    /**
     * {@code POST  /vehiculos} : Create a new vehiculo.
     *
     * @param vehiculoDTO the vehiculoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehiculoDTO, or with status {@code 400 (Bad Request)} if the vehiculo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehiculos")
    public ResponseEntity<VehiculoDTO> createVehiculo(@Valid @RequestBody VehiculoDTO vehiculoDTO) throws URISyntaxException {
        log.debug("REST request to save Vehiculo : {}", vehiculoDTO);
        if (vehiculoDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehiculo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehiculoDTO result = vehiculoService.save(vehiculoDTO);
        return ResponseEntity
            .created(new URI("/api/vehiculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehiculos/:id} : Updates an existing vehiculo.
     *
     * @param id the id of the vehiculoDTO to save.
     * @param vehiculoDTO the vehiculoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehiculoDTO,
     * or with status {@code 400 (Bad Request)} if the vehiculoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehiculoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehiculos/{id}")
    public ResponseEntity<VehiculoDTO> updateVehiculo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehiculoDTO vehiculoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vehiculo : {}, {}", id, vehiculoDTO);
        if (vehiculoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehiculoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VehiculoDTO result = vehiculoService.save(vehiculoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehiculoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vehiculos/:id} : Partial updates given fields of an existing vehiculo, field will ignore if it is null
     *
     * @param id the id of the vehiculoDTO to save.
     * @param vehiculoDTO the vehiculoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehiculoDTO,
     * or with status {@code 400 (Bad Request)} if the vehiculoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehiculoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehiculoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vehiculos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VehiculoDTO> partialUpdateVehiculo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehiculoDTO vehiculoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vehiculo partially : {}, {}", id, vehiculoDTO);
        if (vehiculoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehiculoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehiculoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehiculoDTO> result = vehiculoService.partialUpdate(vehiculoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehiculoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehiculos} : get all the vehiculos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehiculos in body.
     */
    @GetMapping("/vehiculos")
    public ResponseEntity<List<VehiculoDTO>> getAllVehiculos(VehiculoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Vehiculos by criteria: {}", criteria);
        Page<VehiculoDTO> page = vehiculoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehiculos/count} : count all the vehiculos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vehiculos/count")
    public ResponseEntity<Long> countVehiculos(VehiculoCriteria criteria) {
        log.debug("REST request to count Vehiculos by criteria: {}", criteria);
        return ResponseEntity.ok().body(vehiculoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vehiculos/:id} : get the "id" vehiculo.
     *
     * @param id the id of the vehiculoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehiculoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehiculos/{id}")
    public ResponseEntity<VehiculoDTO> getVehiculo(@PathVariable Long id) {
        log.debug("REST request to get Vehiculo : {}", id);
        Optional<VehiculoDTO> vehiculoDTO = vehiculoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehiculoDTO);
    }

    /**
     * {@code DELETE  /vehiculos/:id} : delete the "id" vehiculo.
     *
     * @param id the id of the vehiculoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehiculos/{id}")
    public ResponseEntity<Void> deleteVehiculo(@PathVariable Long id) {
        log.debug("REST request to delete Vehiculo : {}", id);
        vehiculoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
