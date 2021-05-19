package es.florida.web.rest;

import es.florida.repository.RegistroRepository;
import es.florida.service.MailService;
import es.florida.service.RegistroQueryService;
import es.florida.service.RegistroService;
import es.florida.service.VehiculoService;
import es.florida.service.criteria.RegistroCriteria;
import es.florida.service.dto.ClienteDTO;
import es.florida.service.dto.RegistroDTO;
import es.florida.service.dto.VehiculoDTO;
import es.florida.service.impl.ClienteServiceImpl;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link es.florida.domain.Registro}.
 */
@RestController
@RequestMapping("/api")
public class RegistroResource {

    private final Logger log = LoggerFactory.getLogger(RegistroResource.class);

    private static final String ENTITY_NAME = "registro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistroService registroService;

    private final RegistroRepository registroRepository;

    private final RegistroQueryService registroQueryService;

    private final MailService mailService;

    private final ClienteServiceImpl clienteService;

    private final VehiculoService vehiculoService;

    public RegistroResource(
        RegistroService registroService,
        RegistroRepository registroRepository,
        RegistroQueryService registroQueryService,
        MailService mailService, ClienteServiceImpl clienteService, VehiculoService vehiculoService) {
        this.registroService = registroService;
        this.registroRepository = registroRepository;
        this.registroQueryService = registroQueryService;
        this.mailService = mailService;
        this.clienteService = clienteService;
        this.vehiculoService = vehiculoService;
    }

    /**
     * {@code POST  /registros} : Create a new registro.
     *
     * @param registroDTO the registroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registroDTO, or with status {@code 400 (Bad Request)} if the registro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registros")
    public ResponseEntity<RegistroDTO> createRegistro(@Valid @RequestBody RegistroDTO registroDTO) throws URISyntaxException {
        log.debug("REST request to save Registro : {}", registroDTO);
        if (registroDTO.getId() != null) {
            throw new BadRequestAlertException("A new registro cannot already have an ID", ENTITY_NAME, "idexists");
        }

        RegistroDTO result = registroService.save(registroDTO);

        Optional<VehiculoDTO> vehiculoDTO = vehiculoService.findOne(registroDTO.getVehiculo().getId());
        Optional<ClienteDTO> clienteDTO = clienteService.findOne(vehiculoDTO.get().getCliente().getId());


       String contenido = "Hola " + clienteDTO.get().getNombre() + " su vehículo ha cambiado de estado. Podrá verlo en la app. Gracias.";
       mailService.sendEmail(clienteDTO.get().getCorreo(),"Cambio estado vehículo",contenido,false, true);

        return ResponseEntity
            .created(new URI("/api/registros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registros/:id} : Updates an existing registro.
     *
     * @param id the id of the registroDTO to save.
     * @param registroDTO the registroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroDTO,
     * or with status {@code 400 (Bad Request)} if the registroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registros/{id}")
    public ResponseEntity<RegistroDTO> updateRegistro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RegistroDTO registroDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Registro : {}, {}", id, registroDTO);
        if (registroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RegistroDTO result = registroService.save(registroDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, registroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /registros/:id} : Partial updates given fields of an existing registro, field will ignore if it is null
     *
     * @param id the id of the registroDTO to save.
     * @param registroDTO the registroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroDTO,
     * or with status {@code 400 (Bad Request)} if the registroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the registroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the registroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/registros/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RegistroDTO> partialUpdateRegistro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RegistroDTO registroDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Registro partially : {}, {}", id, registroDTO);
        if (registroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RegistroDTO> result = registroService.partialUpdate(registroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, registroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /registros} : get all the registros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registros in body.
     */
    @GetMapping("/registros")
    public ResponseEntity<List<RegistroDTO>> getAllRegistros(RegistroCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Registros by criteria: {}", criteria);
        Page<RegistroDTO> page = registroQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /registros/count} : count all the registros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/registros/count")
    public ResponseEntity<Long> countRegistros(RegistroCriteria criteria) {
        log.debug("REST request to count Registros by criteria: {}", criteria);
        return ResponseEntity.ok().body(registroQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /registros/:id} : get the "id" registro.
     *
     * @param id the id of the registroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registros/{id}")
    public ResponseEntity<RegistroDTO> getRegistro(@PathVariable Long id) {
        log.debug("REST request to get Registro : {}", id);
        Optional<RegistroDTO> registroDTO = registroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registroDTO);
    }

    /**
     * {@code DELETE  /registros/:id} : delete the "id" registro.
     *
     * @param id the id of the registroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registros/{id}")
    public ResponseEntity<Void> deleteRegistro(@PathVariable Long id) {
        log.debug("REST request to delete Registro : {}", id);
        registroService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
