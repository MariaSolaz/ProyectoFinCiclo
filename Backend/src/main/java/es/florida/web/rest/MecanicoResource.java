package es.florida.web.rest;

import es.florida.repository.MecanicoRepository;
import es.florida.service.MecanicoQueryService;
import es.florida.service.MecanicoService;
import es.florida.service.criteria.MecanicoCriteria;
import es.florida.service.dto.MecanicoDTO;
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
 * REST controller for managing {@link es.florida.domain.Mecanico}.
 */
@RestController
@RequestMapping("/api")
public class MecanicoResource {

    private final Logger log = LoggerFactory.getLogger(MecanicoResource.class);

    private static final String ENTITY_NAME = "mecanico";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MecanicoService mecanicoService;

    private final MecanicoRepository mecanicoRepository;

    private final MecanicoQueryService mecanicoQueryService;

    public MecanicoResource(
        MecanicoService mecanicoService,
        MecanicoRepository mecanicoRepository,
        MecanicoQueryService mecanicoQueryService
    ) {
        this.mecanicoService = mecanicoService;
        this.mecanicoRepository = mecanicoRepository;
        this.mecanicoQueryService = mecanicoQueryService;
    }

    /**
     * {@code POST  /mecanicos} : Create a new mecanico.
     *
     * @param mecanicoDTO the mecanicoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mecanicoDTO, or with status {@code 400 (Bad Request)} if the mecanico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mecanicos")
    public ResponseEntity<MecanicoDTO> createMecanico(@Valid @RequestBody MecanicoDTO mecanicoDTO) throws URISyntaxException {
        log.debug("REST request to save Mecanico : {}", mecanicoDTO);
        if (mecanicoDTO.getId() != null) {
            throw new BadRequestAlertException("A new mecanico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MecanicoDTO result = mecanicoService.save(mecanicoDTO);
        return ResponseEntity
            .created(new URI("/api/mecanicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mecanicos/:id} : Updates an existing mecanico.
     *
     * @param id the id of the mecanicoDTO to save.
     * @param mecanicoDTO the mecanicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mecanicoDTO,
     * or with status {@code 400 (Bad Request)} if the mecanicoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mecanicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mecanicos/{id}")
    public ResponseEntity<MecanicoDTO> updateMecanico(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MecanicoDTO mecanicoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Mecanico : {}, {}", id, mecanicoDTO);
        if (mecanicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mecanicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mecanicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MecanicoDTO result = mecanicoService.save(mecanicoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mecanicoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mecanicos/:id} : Partial updates given fields of an existing mecanico, field will ignore if it is null
     *
     * @param id the id of the mecanicoDTO to save.
     * @param mecanicoDTO the mecanicoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mecanicoDTO,
     * or with status {@code 400 (Bad Request)} if the mecanicoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mecanicoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mecanicoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mecanicos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MecanicoDTO> partialUpdateMecanico(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MecanicoDTO mecanicoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mecanico partially : {}, {}", id, mecanicoDTO);
        if (mecanicoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mecanicoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mecanicoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MecanicoDTO> result = mecanicoService.partialUpdate(mecanicoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mecanicoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mecanicos} : get all the mecanicos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mecanicos in body.
     */
    @GetMapping("/mecanicos")
    public ResponseEntity<List<MecanicoDTO>> getAllMecanicos(MecanicoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Mecanicos by criteria: {}", criteria);
        Page<MecanicoDTO> page = mecanicoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mecanicos/count} : count all the mecanicos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mecanicos/count")
    public ResponseEntity<Long> countMecanicos(MecanicoCriteria criteria) {
        log.debug("REST request to count Mecanicos by criteria: {}", criteria);
        return ResponseEntity.ok().body(mecanicoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mecanicos/:id} : get the "id" mecanico.
     *
     * @param id the id of the mecanicoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mecanicoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mecanicos/{id}")
    public ResponseEntity<MecanicoDTO> getMecanico(@PathVariable Long id) {
        log.debug("REST request to get Mecanico : {}", id);
        Optional<MecanicoDTO> mecanicoDTO = mecanicoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mecanicoDTO);
    }

    /**
     * {@code DELETE  /mecanicos/:id} : delete the "id" mecanico.
     *
     * @param id the id of the mecanicoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mecanicos/{id}")
    public ResponseEntity<Void> deleteMecanico(@PathVariable Long id) {
        log.debug("REST request to delete Mecanico : {}", id);
        mecanicoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
