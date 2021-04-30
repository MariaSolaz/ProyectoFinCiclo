package es.florida.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.florida.IntegrationTest;
import es.florida.domain.Mecanico;
import es.florida.domain.Vehiculo;
import es.florida.repository.MecanicoRepository;
import es.florida.service.criteria.MecanicoCriteria;
import es.florida.service.dto.MecanicoDTO;
import es.florida.service.mapper.MecanicoMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MecanicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MecanicoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_D_NI = "AAAAAAAAAA";
    private static final String UPDATED_D_NI = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mecanicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private MecanicoMapper mecanicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMecanicoMockMvc;

    private Mecanico mecanico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mecanico createEntity(EntityManager em) {
        Mecanico mecanico = new Mecanico()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .dNI(DEFAULT_D_NI)
            .telefono(DEFAULT_TELEFONO)
            .correo(DEFAULT_CORREO);
        return mecanico;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mecanico createUpdatedEntity(EntityManager em) {
        Mecanico mecanico = new Mecanico()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .dNI(UPDATED_D_NI)
            .telefono(UPDATED_TELEFONO)
            .correo(UPDATED_CORREO);
        return mecanico;
    }

    @BeforeEach
    public void initTest() {
        mecanico = createEntity(em);
    }

    @Test
    @Transactional
    void createMecanico() throws Exception {
        int databaseSizeBeforeCreate = mecanicoRepository.findAll().size();
        // Create the Mecanico
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);
        restMecanicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mecanicoDTO)))
            .andExpect(status().isCreated());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeCreate + 1);
        Mecanico testMecanico = mecanicoList.get(mecanicoList.size() - 1);
        assertThat(testMecanico.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMecanico.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testMecanico.getdNI()).isEqualTo(DEFAULT_D_NI);
        assertThat(testMecanico.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testMecanico.getCorreo()).isEqualTo(DEFAULT_CORREO);
    }

    @Test
    @Transactional
    void createMecanicoWithExistingId() throws Exception {
        // Create the Mecanico with an existing ID
        mecanico.setId(1L);
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        int databaseSizeBeforeCreate = mecanicoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMecanicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mecanicoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = mecanicoRepository.findAll().size();
        // set the field null
        mecanico.setNombre(null);

        // Create the Mecanico, which fails.
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        restMecanicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mecanicoDTO)))
            .andExpect(status().isBadRequest());

        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = mecanicoRepository.findAll().size();
        // set the field null
        mecanico.setApellido(null);

        // Create the Mecanico, which fails.
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        restMecanicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mecanicoDTO)))
            .andExpect(status().isBadRequest());

        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkdNIIsRequired() throws Exception {
        int databaseSizeBeforeTest = mecanicoRepository.findAll().size();
        // set the field null
        mecanico.setdNI(null);

        // Create the Mecanico, which fails.
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        restMecanicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mecanicoDTO)))
            .andExpect(status().isBadRequest());

        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = mecanicoRepository.findAll().size();
        // set the field null
        mecanico.setTelefono(null);

        // Create the Mecanico, which fails.
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        restMecanicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mecanicoDTO)))
            .andExpect(status().isBadRequest());

        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCorreoIsRequired() throws Exception {
        int databaseSizeBeforeTest = mecanicoRepository.findAll().size();
        // set the field null
        mecanico.setCorreo(null);

        // Create the Mecanico, which fails.
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        restMecanicoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mecanicoDTO)))
            .andExpect(status().isBadRequest());

        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMecanicos() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList
        restMecanicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mecanico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].dNI").value(hasItem(DEFAULT_D_NI)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)));
    }

    @Test
    @Transactional
    void getMecanico() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get the mecanico
        restMecanicoMockMvc
            .perform(get(ENTITY_API_URL_ID, mecanico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mecanico.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.dNI").value(DEFAULT_D_NI))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.correo").value(DEFAULT_CORREO));
    }

    @Test
    @Transactional
    void getMecanicosByIdFiltering() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        Long id = mecanico.getId();

        defaultMecanicoShouldBeFound("id.equals=" + id);
        defaultMecanicoShouldNotBeFound("id.notEquals=" + id);

        defaultMecanicoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMecanicoShouldNotBeFound("id.greaterThan=" + id);

        defaultMecanicoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMecanicoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMecanicosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where nombre equals to DEFAULT_NOMBRE
        defaultMecanicoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the mecanicoList where nombre equals to UPDATED_NOMBRE
        defaultMecanicoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMecanicosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where nombre not equals to DEFAULT_NOMBRE
        defaultMecanicoShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the mecanicoList where nombre not equals to UPDATED_NOMBRE
        defaultMecanicoShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMecanicosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultMecanicoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the mecanicoList where nombre equals to UPDATED_NOMBRE
        defaultMecanicoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMecanicosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where nombre is not null
        defaultMecanicoShouldBeFound("nombre.specified=true");

        // Get all the mecanicoList where nombre is null
        defaultMecanicoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllMecanicosByNombreContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where nombre contains DEFAULT_NOMBRE
        defaultMecanicoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the mecanicoList where nombre contains UPDATED_NOMBRE
        defaultMecanicoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMecanicosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where nombre does not contain DEFAULT_NOMBRE
        defaultMecanicoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the mecanicoList where nombre does not contain UPDATED_NOMBRE
        defaultMecanicoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMecanicosByApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where apellido equals to DEFAULT_APELLIDO
        defaultMecanicoShouldBeFound("apellido.equals=" + DEFAULT_APELLIDO);

        // Get all the mecanicoList where apellido equals to UPDATED_APELLIDO
        defaultMecanicoShouldNotBeFound("apellido.equals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllMecanicosByApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where apellido not equals to DEFAULT_APELLIDO
        defaultMecanicoShouldNotBeFound("apellido.notEquals=" + DEFAULT_APELLIDO);

        // Get all the mecanicoList where apellido not equals to UPDATED_APELLIDO
        defaultMecanicoShouldBeFound("apellido.notEquals=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllMecanicosByApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where apellido in DEFAULT_APELLIDO or UPDATED_APELLIDO
        defaultMecanicoShouldBeFound("apellido.in=" + DEFAULT_APELLIDO + "," + UPDATED_APELLIDO);

        // Get all the mecanicoList where apellido equals to UPDATED_APELLIDO
        defaultMecanicoShouldNotBeFound("apellido.in=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllMecanicosByApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where apellido is not null
        defaultMecanicoShouldBeFound("apellido.specified=true");

        // Get all the mecanicoList where apellido is null
        defaultMecanicoShouldNotBeFound("apellido.specified=false");
    }

    @Test
    @Transactional
    void getAllMecanicosByApellidoContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where apellido contains DEFAULT_APELLIDO
        defaultMecanicoShouldBeFound("apellido.contains=" + DEFAULT_APELLIDO);

        // Get all the mecanicoList where apellido contains UPDATED_APELLIDO
        defaultMecanicoShouldNotBeFound("apellido.contains=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllMecanicosByApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where apellido does not contain DEFAULT_APELLIDO
        defaultMecanicoShouldNotBeFound("apellido.doesNotContain=" + DEFAULT_APELLIDO);

        // Get all the mecanicoList where apellido does not contain UPDATED_APELLIDO
        defaultMecanicoShouldBeFound("apellido.doesNotContain=" + UPDATED_APELLIDO);
    }

    @Test
    @Transactional
    void getAllMecanicosBydNIIsEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where dNI equals to DEFAULT_D_NI
        defaultMecanicoShouldBeFound("dNI.equals=" + DEFAULT_D_NI);

        // Get all the mecanicoList where dNI equals to UPDATED_D_NI
        defaultMecanicoShouldNotBeFound("dNI.equals=" + UPDATED_D_NI);
    }

    @Test
    @Transactional
    void getAllMecanicosBydNIIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where dNI not equals to DEFAULT_D_NI
        defaultMecanicoShouldNotBeFound("dNI.notEquals=" + DEFAULT_D_NI);

        // Get all the mecanicoList where dNI not equals to UPDATED_D_NI
        defaultMecanicoShouldBeFound("dNI.notEquals=" + UPDATED_D_NI);
    }

    @Test
    @Transactional
    void getAllMecanicosBydNIIsInShouldWork() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where dNI in DEFAULT_D_NI or UPDATED_D_NI
        defaultMecanicoShouldBeFound("dNI.in=" + DEFAULT_D_NI + "," + UPDATED_D_NI);

        // Get all the mecanicoList where dNI equals to UPDATED_D_NI
        defaultMecanicoShouldNotBeFound("dNI.in=" + UPDATED_D_NI);
    }

    @Test
    @Transactional
    void getAllMecanicosBydNIIsNullOrNotNull() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where dNI is not null
        defaultMecanicoShouldBeFound("dNI.specified=true");

        // Get all the mecanicoList where dNI is null
        defaultMecanicoShouldNotBeFound("dNI.specified=false");
    }

    @Test
    @Transactional
    void getAllMecanicosBydNIContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where dNI contains DEFAULT_D_NI
        defaultMecanicoShouldBeFound("dNI.contains=" + DEFAULT_D_NI);

        // Get all the mecanicoList where dNI contains UPDATED_D_NI
        defaultMecanicoShouldNotBeFound("dNI.contains=" + UPDATED_D_NI);
    }

    @Test
    @Transactional
    void getAllMecanicosBydNINotContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where dNI does not contain DEFAULT_D_NI
        defaultMecanicoShouldNotBeFound("dNI.doesNotContain=" + DEFAULT_D_NI);

        // Get all the mecanicoList where dNI does not contain UPDATED_D_NI
        defaultMecanicoShouldBeFound("dNI.doesNotContain=" + UPDATED_D_NI);
    }

    @Test
    @Transactional
    void getAllMecanicosByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where telefono equals to DEFAULT_TELEFONO
        defaultMecanicoShouldBeFound("telefono.equals=" + DEFAULT_TELEFONO);

        // Get all the mecanicoList where telefono equals to UPDATED_TELEFONO
        defaultMecanicoShouldNotBeFound("telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllMecanicosByTelefonoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where telefono not equals to DEFAULT_TELEFONO
        defaultMecanicoShouldNotBeFound("telefono.notEquals=" + DEFAULT_TELEFONO);

        // Get all the mecanicoList where telefono not equals to UPDATED_TELEFONO
        defaultMecanicoShouldBeFound("telefono.notEquals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllMecanicosByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where telefono in DEFAULT_TELEFONO or UPDATED_TELEFONO
        defaultMecanicoShouldBeFound("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO);

        // Get all the mecanicoList where telefono equals to UPDATED_TELEFONO
        defaultMecanicoShouldNotBeFound("telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllMecanicosByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where telefono is not null
        defaultMecanicoShouldBeFound("telefono.specified=true");

        // Get all the mecanicoList where telefono is null
        defaultMecanicoShouldNotBeFound("telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllMecanicosByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where telefono contains DEFAULT_TELEFONO
        defaultMecanicoShouldBeFound("telefono.contains=" + DEFAULT_TELEFONO);

        // Get all the mecanicoList where telefono contains UPDATED_TELEFONO
        defaultMecanicoShouldNotBeFound("telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllMecanicosByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where telefono does not contain DEFAULT_TELEFONO
        defaultMecanicoShouldNotBeFound("telefono.doesNotContain=" + DEFAULT_TELEFONO);

        // Get all the mecanicoList where telefono does not contain UPDATED_TELEFONO
        defaultMecanicoShouldBeFound("telefono.doesNotContain=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllMecanicosByCorreoIsEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where correo equals to DEFAULT_CORREO
        defaultMecanicoShouldBeFound("correo.equals=" + DEFAULT_CORREO);

        // Get all the mecanicoList where correo equals to UPDATED_CORREO
        defaultMecanicoShouldNotBeFound("correo.equals=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllMecanicosByCorreoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where correo not equals to DEFAULT_CORREO
        defaultMecanicoShouldNotBeFound("correo.notEquals=" + DEFAULT_CORREO);

        // Get all the mecanicoList where correo not equals to UPDATED_CORREO
        defaultMecanicoShouldBeFound("correo.notEquals=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllMecanicosByCorreoIsInShouldWork() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where correo in DEFAULT_CORREO or UPDATED_CORREO
        defaultMecanicoShouldBeFound("correo.in=" + DEFAULT_CORREO + "," + UPDATED_CORREO);

        // Get all the mecanicoList where correo equals to UPDATED_CORREO
        defaultMecanicoShouldNotBeFound("correo.in=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllMecanicosByCorreoIsNullOrNotNull() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where correo is not null
        defaultMecanicoShouldBeFound("correo.specified=true");

        // Get all the mecanicoList where correo is null
        defaultMecanicoShouldNotBeFound("correo.specified=false");
    }

    @Test
    @Transactional
    void getAllMecanicosByCorreoContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where correo contains DEFAULT_CORREO
        defaultMecanicoShouldBeFound("correo.contains=" + DEFAULT_CORREO);

        // Get all the mecanicoList where correo contains UPDATED_CORREO
        defaultMecanicoShouldNotBeFound("correo.contains=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllMecanicosByCorreoNotContainsSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        // Get all the mecanicoList where correo does not contain DEFAULT_CORREO
        defaultMecanicoShouldNotBeFound("correo.doesNotContain=" + DEFAULT_CORREO);

        // Get all the mecanicoList where correo does not contain UPDATED_CORREO
        defaultMecanicoShouldBeFound("correo.doesNotContain=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllMecanicosByVehiculoIsEqualToSomething() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);
        Vehiculo vehiculo = VehiculoResourceIT.createEntity(em);
        em.persist(vehiculo);
        em.flush();
        mecanico.setVehiculo(vehiculo);
        mecanicoRepository.saveAndFlush(mecanico);
        Long vehiculoId = vehiculo.getId();

        // Get all the mecanicoList where vehiculo equals to vehiculoId
        defaultMecanicoShouldBeFound("vehiculoId.equals=" + vehiculoId);

        // Get all the mecanicoList where vehiculo equals to (vehiculoId + 1)
        defaultMecanicoShouldNotBeFound("vehiculoId.equals=" + (vehiculoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMecanicoShouldBeFound(String filter) throws Exception {
        restMecanicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mecanico.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].dNI").value(hasItem(DEFAULT_D_NI)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)));

        // Check, that the count call also returns 1
        restMecanicoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMecanicoShouldNotBeFound(String filter) throws Exception {
        restMecanicoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMecanicoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMecanico() throws Exception {
        // Get the mecanico
        restMecanicoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMecanico() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();

        // Update the mecanico
        Mecanico updatedMecanico = mecanicoRepository.findById(mecanico.getId()).get();
        // Disconnect from session so that the updates on updatedMecanico are not directly saved in db
        em.detach(updatedMecanico);
        updatedMecanico
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .dNI(UPDATED_D_NI)
            .telefono(UPDATED_TELEFONO)
            .correo(UPDATED_CORREO);
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(updatedMecanico);

        restMecanicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mecanicoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mecanicoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
        Mecanico testMecanico = mecanicoList.get(mecanicoList.size() - 1);
        assertThat(testMecanico.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMecanico.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testMecanico.getdNI()).isEqualTo(UPDATED_D_NI);
        assertThat(testMecanico.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testMecanico.getCorreo()).isEqualTo(UPDATED_CORREO);
    }

    @Test
    @Transactional
    void putNonExistingMecanico() throws Exception {
        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();
        mecanico.setId(count.incrementAndGet());

        // Create the Mecanico
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMecanicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mecanicoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mecanicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMecanico() throws Exception {
        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();
        mecanico.setId(count.incrementAndGet());

        // Create the Mecanico
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMecanicoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mecanicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMecanico() throws Exception {
        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();
        mecanico.setId(count.incrementAndGet());

        // Create the Mecanico
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMecanicoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mecanicoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMecanicoWithPatch() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();

        // Update the mecanico using partial update
        Mecanico partialUpdatedMecanico = new Mecanico();
        partialUpdatedMecanico.setId(mecanico.getId());

        partialUpdatedMecanico.telefono(UPDATED_TELEFONO);

        restMecanicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMecanico.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMecanico))
            )
            .andExpect(status().isOk());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
        Mecanico testMecanico = mecanicoList.get(mecanicoList.size() - 1);
        assertThat(testMecanico.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMecanico.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testMecanico.getdNI()).isEqualTo(DEFAULT_D_NI);
        assertThat(testMecanico.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testMecanico.getCorreo()).isEqualTo(DEFAULT_CORREO);
    }

    @Test
    @Transactional
    void fullUpdateMecanicoWithPatch() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();

        // Update the mecanico using partial update
        Mecanico partialUpdatedMecanico = new Mecanico();
        partialUpdatedMecanico.setId(mecanico.getId());

        partialUpdatedMecanico
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .dNI(UPDATED_D_NI)
            .telefono(UPDATED_TELEFONO)
            .correo(UPDATED_CORREO);

        restMecanicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMecanico.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMecanico))
            )
            .andExpect(status().isOk());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
        Mecanico testMecanico = mecanicoList.get(mecanicoList.size() - 1);
        assertThat(testMecanico.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMecanico.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testMecanico.getdNI()).isEqualTo(UPDATED_D_NI);
        assertThat(testMecanico.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testMecanico.getCorreo()).isEqualTo(UPDATED_CORREO);
    }

    @Test
    @Transactional
    void patchNonExistingMecanico() throws Exception {
        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();
        mecanico.setId(count.incrementAndGet());

        // Create the Mecanico
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMecanicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mecanicoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mecanicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMecanico() throws Exception {
        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();
        mecanico.setId(count.incrementAndGet());

        // Create the Mecanico
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMecanicoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mecanicoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMecanico() throws Exception {
        int databaseSizeBeforeUpdate = mecanicoRepository.findAll().size();
        mecanico.setId(count.incrementAndGet());

        // Create the Mecanico
        MecanicoDTO mecanicoDTO = mecanicoMapper.toDto(mecanico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMecanicoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mecanicoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mecanico in the database
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMecanico() throws Exception {
        // Initialize the database
        mecanicoRepository.saveAndFlush(mecanico);

        int databaseSizeBeforeDelete = mecanicoRepository.findAll().size();

        // Delete the mecanico
        restMecanicoMockMvc
            .perform(delete(ENTITY_API_URL_ID, mecanico.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mecanico> mecanicoList = mecanicoRepository.findAll();
        assertThat(mecanicoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
