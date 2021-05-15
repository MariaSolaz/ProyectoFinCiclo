package es.florida.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.florida.IntegrationTest;
import es.florida.domain.Cliente;
import es.florida.domain.Factura;
import es.florida.domain.Mecanico;
import es.florida.domain.Registro;
import es.florida.domain.Vehiculo;
import es.florida.domain.enumeration.EstadoVehiculo;
import es.florida.repository.VehiculoRepository;
import es.florida.service.criteria.VehiculoCriteria;
import es.florida.service.dto.VehiculoDTO;
import es.florida.service.mapper.VehiculoMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link VehiculoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehiculoResourceIT {

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ANYO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANYO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ANYO = LocalDate.ofEpochDay(-1L);

    private static final EstadoVehiculo DEFAULT_ESTADO = EstadoVehiculo.NoRevisado;
    private static final EstadoVehiculo UPDATED_ESTADO = EstadoVehiculo.Revisado;

    private static final String ENTITY_API_URL = "/api/vehiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private VehiculoMapper vehiculoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehiculoMockMvc;

    private Vehiculo vehiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehiculo createEntity(EntityManager em) {
        Vehiculo vehiculo = new Vehiculo()
            .matricula(DEFAULT_MATRICULA)
            .marca(DEFAULT_MARCA)
            .modelo(DEFAULT_MODELO)
            .anyo(DEFAULT_ANYO)
            .estado(DEFAULT_ESTADO);
        return vehiculo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehiculo createUpdatedEntity(EntityManager em) {
        Vehiculo vehiculo = new Vehiculo()
            .matricula(UPDATED_MATRICULA)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .anyo(UPDATED_ANYO)
            .estado(UPDATED_ESTADO);
        return vehiculo;
    }

    @BeforeEach
    public void initTest() {
        vehiculo = createEntity(em);
    }

    @Test
    @Transactional
    void createVehiculo() throws Exception {
        int databaseSizeBeforeCreate = vehiculoRepository.findAll().size();
        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);
        restVehiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isCreated());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeCreate + 1);
        Vehiculo testVehiculo = vehiculoList.get(vehiculoList.size() - 1);
        assertThat(testVehiculo.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testVehiculo.getMarca()).isEqualTo(DEFAULT_MARCA);
        assertThat(testVehiculo.getModelo()).isEqualTo(DEFAULT_MODELO);
        assertThat(testVehiculo.getAnyo()).isEqualTo(DEFAULT_ANYO);
        assertThat(testVehiculo.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createVehiculoWithExistingId() throws Exception {
        // Create the Vehiculo with an existing ID
        vehiculo.setId(1L);
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        int databaseSizeBeforeCreate = vehiculoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculaIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculoRepository.findAll().size();
        // set the field null
        vehiculo.setMatricula(null);

        // Create the Vehiculo, which fails.
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        restVehiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMarcaIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculoRepository.findAll().size();
        // set the field null
        vehiculo.setMarca(null);

        // Create the Vehiculo, which fails.
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        restVehiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModeloIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculoRepository.findAll().size();
        // set the field null
        vehiculo.setModelo(null);

        // Create the Vehiculo, which fails.
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        restVehiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnyoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculoRepository.findAll().size();
        // set the field null
        vehiculo.setAnyo(null);

        // Create the Vehiculo, which fails.
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        restVehiculoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isBadRequest());

        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehiculos() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList
        restVehiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].anyo").value(hasItem(DEFAULT_ANYO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getVehiculo() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get the vehiculo
        restVehiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, vehiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehiculo.getId().intValue()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.anyo").value(DEFAULT_ANYO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getVehiculosByIdFiltering() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        Long id = vehiculo.getId();

        defaultVehiculoShouldBeFound("id.equals=" + id);
        defaultVehiculoShouldNotBeFound("id.notEquals=" + id);

        defaultVehiculoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVehiculoShouldNotBeFound("id.greaterThan=" + id);

        defaultVehiculoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVehiculoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVehiculosByMatriculaIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where matricula equals to DEFAULT_MATRICULA
        defaultVehiculoShouldBeFound("matricula.equals=" + DEFAULT_MATRICULA);

        // Get all the vehiculoList where matricula equals to UPDATED_MATRICULA
        defaultVehiculoShouldNotBeFound("matricula.equals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMatriculaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where matricula not equals to DEFAULT_MATRICULA
        defaultVehiculoShouldNotBeFound("matricula.notEquals=" + DEFAULT_MATRICULA);

        // Get all the vehiculoList where matricula not equals to UPDATED_MATRICULA
        defaultVehiculoShouldBeFound("matricula.notEquals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMatriculaIsInShouldWork() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where matricula in DEFAULT_MATRICULA or UPDATED_MATRICULA
        defaultVehiculoShouldBeFound("matricula.in=" + DEFAULT_MATRICULA + "," + UPDATED_MATRICULA);

        // Get all the vehiculoList where matricula equals to UPDATED_MATRICULA
        defaultVehiculoShouldNotBeFound("matricula.in=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMatriculaIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where matricula is not null
        defaultVehiculoShouldBeFound("matricula.specified=true");

        // Get all the vehiculoList where matricula is null
        defaultVehiculoShouldNotBeFound("matricula.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiculosByMatriculaContainsSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where matricula contains DEFAULT_MATRICULA
        defaultVehiculoShouldBeFound("matricula.contains=" + DEFAULT_MATRICULA);

        // Get all the vehiculoList where matricula contains UPDATED_MATRICULA
        defaultVehiculoShouldNotBeFound("matricula.contains=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMatriculaNotContainsSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where matricula does not contain DEFAULT_MATRICULA
        defaultVehiculoShouldNotBeFound("matricula.doesNotContain=" + DEFAULT_MATRICULA);

        // Get all the vehiculoList where matricula does not contain UPDATED_MATRICULA
        defaultVehiculoShouldBeFound("matricula.doesNotContain=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMarcaIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where marca equals to DEFAULT_MARCA
        defaultVehiculoShouldBeFound("marca.equals=" + DEFAULT_MARCA);

        // Get all the vehiculoList where marca equals to UPDATED_MARCA
        defaultVehiculoShouldNotBeFound("marca.equals=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMarcaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where marca not equals to DEFAULT_MARCA
        defaultVehiculoShouldNotBeFound("marca.notEquals=" + DEFAULT_MARCA);

        // Get all the vehiculoList where marca not equals to UPDATED_MARCA
        defaultVehiculoShouldBeFound("marca.notEquals=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMarcaIsInShouldWork() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where marca in DEFAULT_MARCA or UPDATED_MARCA
        defaultVehiculoShouldBeFound("marca.in=" + DEFAULT_MARCA + "," + UPDATED_MARCA);

        // Get all the vehiculoList where marca equals to UPDATED_MARCA
        defaultVehiculoShouldNotBeFound("marca.in=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMarcaIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where marca is not null
        defaultVehiculoShouldBeFound("marca.specified=true");

        // Get all the vehiculoList where marca is null
        defaultVehiculoShouldNotBeFound("marca.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiculosByMarcaContainsSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where marca contains DEFAULT_MARCA
        defaultVehiculoShouldBeFound("marca.contains=" + DEFAULT_MARCA);

        // Get all the vehiculoList where marca contains UPDATED_MARCA
        defaultVehiculoShouldNotBeFound("marca.contains=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllVehiculosByMarcaNotContainsSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where marca does not contain DEFAULT_MARCA
        defaultVehiculoShouldNotBeFound("marca.doesNotContain=" + DEFAULT_MARCA);

        // Get all the vehiculoList where marca does not contain UPDATED_MARCA
        defaultVehiculoShouldBeFound("marca.doesNotContain=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllVehiculosByModeloIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where modelo equals to DEFAULT_MODELO
        defaultVehiculoShouldBeFound("modelo.equals=" + DEFAULT_MODELO);

        // Get all the vehiculoList where modelo equals to UPDATED_MODELO
        defaultVehiculoShouldNotBeFound("modelo.equals=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVehiculosByModeloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where modelo not equals to DEFAULT_MODELO
        defaultVehiculoShouldNotBeFound("modelo.notEquals=" + DEFAULT_MODELO);

        // Get all the vehiculoList where modelo not equals to UPDATED_MODELO
        defaultVehiculoShouldBeFound("modelo.notEquals=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVehiculosByModeloIsInShouldWork() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where modelo in DEFAULT_MODELO or UPDATED_MODELO
        defaultVehiculoShouldBeFound("modelo.in=" + DEFAULT_MODELO + "," + UPDATED_MODELO);

        // Get all the vehiculoList where modelo equals to UPDATED_MODELO
        defaultVehiculoShouldNotBeFound("modelo.in=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVehiculosByModeloIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where modelo is not null
        defaultVehiculoShouldBeFound("modelo.specified=true");

        // Get all the vehiculoList where modelo is null
        defaultVehiculoShouldNotBeFound("modelo.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiculosByModeloContainsSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where modelo contains DEFAULT_MODELO
        defaultVehiculoShouldBeFound("modelo.contains=" + DEFAULT_MODELO);

        // Get all the vehiculoList where modelo contains UPDATED_MODELO
        defaultVehiculoShouldNotBeFound("modelo.contains=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVehiculosByModeloNotContainsSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where modelo does not contain DEFAULT_MODELO
        defaultVehiculoShouldNotBeFound("modelo.doesNotContain=" + DEFAULT_MODELO);

        // Get all the vehiculoList where modelo does not contain UPDATED_MODELO
        defaultVehiculoShouldBeFound("modelo.doesNotContain=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllVehiculosByAnyoIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where anyo equals to DEFAULT_ANYO
        defaultVehiculoShouldBeFound("anyo.equals=" + DEFAULT_ANYO);

        // Get all the vehiculoList where anyo equals to UPDATED_ANYO
        defaultVehiculoShouldNotBeFound("anyo.equals=" + UPDATED_ANYO);
    }

    @Test
    @Transactional
    void getAllVehiculosByAnyoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where anyo not equals to DEFAULT_ANYO
        defaultVehiculoShouldNotBeFound("anyo.notEquals=" + DEFAULT_ANYO);

        // Get all the vehiculoList where anyo not equals to UPDATED_ANYO
        defaultVehiculoShouldBeFound("anyo.notEquals=" + UPDATED_ANYO);
    }

    @Test
    @Transactional
    void getAllVehiculosByAnyoIsInShouldWork() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where anyo in DEFAULT_ANYO or UPDATED_ANYO
        defaultVehiculoShouldBeFound("anyo.in=" + DEFAULT_ANYO + "," + UPDATED_ANYO);

        // Get all the vehiculoList where anyo equals to UPDATED_ANYO
        defaultVehiculoShouldNotBeFound("anyo.in=" + UPDATED_ANYO);
    }

    @Test
    @Transactional
    void getAllVehiculosByAnyoIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where anyo is not null
        defaultVehiculoShouldBeFound("anyo.specified=true");

        // Get all the vehiculoList where anyo is null
        defaultVehiculoShouldNotBeFound("anyo.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiculosByAnyoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where anyo is greater than or equal to DEFAULT_ANYO
        defaultVehiculoShouldBeFound("anyo.greaterThanOrEqual=" + DEFAULT_ANYO);

        // Get all the vehiculoList where anyo is greater than or equal to UPDATED_ANYO
        defaultVehiculoShouldNotBeFound("anyo.greaterThanOrEqual=" + UPDATED_ANYO);
    }

    @Test
    @Transactional
    void getAllVehiculosByAnyoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where anyo is less than or equal to DEFAULT_ANYO
        defaultVehiculoShouldBeFound("anyo.lessThanOrEqual=" + DEFAULT_ANYO);

        // Get all the vehiculoList where anyo is less than or equal to SMALLER_ANYO
        defaultVehiculoShouldNotBeFound("anyo.lessThanOrEqual=" + SMALLER_ANYO);
    }

    @Test
    @Transactional
    void getAllVehiculosByAnyoIsLessThanSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where anyo is less than DEFAULT_ANYO
        defaultVehiculoShouldNotBeFound("anyo.lessThan=" + DEFAULT_ANYO);

        // Get all the vehiculoList where anyo is less than UPDATED_ANYO
        defaultVehiculoShouldBeFound("anyo.lessThan=" + UPDATED_ANYO);
    }

    @Test
    @Transactional
    void getAllVehiculosByAnyoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where anyo is greater than DEFAULT_ANYO
        defaultVehiculoShouldNotBeFound("anyo.greaterThan=" + DEFAULT_ANYO);

        // Get all the vehiculoList where anyo is greater than SMALLER_ANYO
        defaultVehiculoShouldBeFound("anyo.greaterThan=" + SMALLER_ANYO);
    }

    @Test
    @Transactional
    void getAllVehiculosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where estado equals to DEFAULT_ESTADO
        defaultVehiculoShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the vehiculoList where estado equals to UPDATED_ESTADO
        defaultVehiculoShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllVehiculosByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where estado not equals to DEFAULT_ESTADO
        defaultVehiculoShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the vehiculoList where estado not equals to UPDATED_ESTADO
        defaultVehiculoShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllVehiculosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultVehiculoShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the vehiculoList where estado equals to UPDATED_ESTADO
        defaultVehiculoShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllVehiculosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        // Get all the vehiculoList where estado is not null
        defaultVehiculoShouldBeFound("estado.specified=true");

        // Get all the vehiculoList where estado is null
        defaultVehiculoShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiculosByRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);
        Registro registro = RegistroResourceIT.createEntity(em);
        em.persist(registro);
        em.flush();
        vehiculo.addRegistro(registro);
        vehiculoRepository.saveAndFlush(vehiculo);
        Long registroId = registro.getId();

        // Get all the vehiculoList where registro equals to registroId
        defaultVehiculoShouldBeFound("registroId.equals=" + registroId);

        // Get all the vehiculoList where registro equals to (registroId + 1)
        defaultVehiculoShouldNotBeFound("registroId.equals=" + (registroId + 1));
    }

    @Test
    @Transactional
    void getAllVehiculosByDuenyoIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);
        Cliente duenyo = ClienteResourceIT.createEntity(em);
        em.persist(duenyo);
        em.flush();
        vehiculo.addDuenyo(duenyo);
        vehiculoRepository.saveAndFlush(vehiculo);
        Long duenyoId = duenyo.getId();

        // Get all the vehiculoList where duenyo equals to duenyoId
        defaultVehiculoShouldBeFound("duenyoId.equals=" + duenyoId);

        // Get all the vehiculoList where duenyo equals to (duenyoId + 1)
        defaultVehiculoShouldNotBeFound("duenyoId.equals=" + (duenyoId + 1));
    }

    @Test
    @Transactional
    void getAllVehiculosByMecanicoIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);
        Mecanico mecanico = MecanicoResourceIT.createEntity(em);
        em.persist(mecanico);
        em.flush();
        vehiculo.addMecanico(mecanico);
        vehiculoRepository.saveAndFlush(vehiculo);
        Long mecanicoId = mecanico.getId();

        // Get all the vehiculoList where mecanico equals to mecanicoId
        defaultVehiculoShouldBeFound("mecanicoId.equals=" + mecanicoId);

        // Get all the vehiculoList where mecanico equals to (mecanicoId + 1)
        defaultVehiculoShouldNotBeFound("mecanicoId.equals=" + (mecanicoId + 1));
    }

    @Test
    @Transactional
    void getAllVehiculosByMatriculaIsEqualToSomething() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);
        Factura matricula = FacturaResourceIT.createEntity(em);
        em.persist(matricula);
        em.flush();
        vehiculo.addMatricula(matricula);
        vehiculoRepository.saveAndFlush(vehiculo);
        Long matriculaId = matricula.getId();

        // Get all the vehiculoList where matricula equals to matriculaId
        defaultVehiculoShouldBeFound("matriculaId.equals=" + matriculaId);

        // Get all the vehiculoList where matricula equals to (matriculaId + 1)
        defaultVehiculoShouldNotBeFound("matriculaId.equals=" + (matriculaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehiculoShouldBeFound(String filter) throws Exception {
        restVehiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].anyo").value(hasItem(DEFAULT_ANYO.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restVehiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehiculoShouldNotBeFound(String filter) throws Exception {
        restVehiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehiculo() throws Exception {
        // Get the vehiculo
        restVehiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVehiculo() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();

        // Update the vehiculo
        Vehiculo updatedVehiculo = vehiculoRepository.findById(vehiculo.getId()).get();
        // Disconnect from session so that the updates on updatedVehiculo are not directly saved in db
        em.detach(updatedVehiculo);
        updatedVehiculo.matricula(UPDATED_MATRICULA).marca(UPDATED_MARCA).modelo(UPDATED_MODELO).anyo(UPDATED_ANYO).estado(UPDATED_ESTADO);
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(updatedVehiculo);

        restVehiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
        Vehiculo testVehiculo = vehiculoList.get(vehiculoList.size() - 1);
        assertThat(testVehiculo.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testVehiculo.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testVehiculo.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testVehiculo.getAnyo()).isEqualTo(UPDATED_ANYO);
        assertThat(testVehiculo.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingVehiculo() throws Exception {
        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();
        vehiculo.setId(count.incrementAndGet());

        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehiculo() throws Exception {
        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();
        vehiculo.setId(count.incrementAndGet());

        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehiculo() throws Exception {
        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();
        vehiculo.setId(count.incrementAndGet());

        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiculoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehiculoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehiculoWithPatch() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();

        // Update the vehiculo using partial update
        Vehiculo partialUpdatedVehiculo = new Vehiculo();
        partialUpdatedVehiculo.setId(vehiculo.getId());

        partialUpdatedVehiculo.matricula(UPDATED_MATRICULA).modelo(UPDATED_MODELO).estado(UPDATED_ESTADO);

        restVehiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehiculo))
            )
            .andExpect(status().isOk());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
        Vehiculo testVehiculo = vehiculoList.get(vehiculoList.size() - 1);
        assertThat(testVehiculo.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testVehiculo.getMarca()).isEqualTo(DEFAULT_MARCA);
        assertThat(testVehiculo.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testVehiculo.getAnyo()).isEqualTo(DEFAULT_ANYO);
        assertThat(testVehiculo.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateVehiculoWithPatch() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();

        // Update the vehiculo using partial update
        Vehiculo partialUpdatedVehiculo = new Vehiculo();
        partialUpdatedVehiculo.setId(vehiculo.getId());

        partialUpdatedVehiculo
            .matricula(UPDATED_MATRICULA)
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .anyo(UPDATED_ANYO)
            .estado(UPDATED_ESTADO);

        restVehiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehiculo))
            )
            .andExpect(status().isOk());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
        Vehiculo testVehiculo = vehiculoList.get(vehiculoList.size() - 1);
        assertThat(testVehiculo.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testVehiculo.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testVehiculo.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testVehiculo.getAnyo()).isEqualTo(UPDATED_ANYO);
        assertThat(testVehiculo.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingVehiculo() throws Exception {
        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();
        vehiculo.setId(count.incrementAndGet());

        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehiculoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehiculo() throws Exception {
        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();
        vehiculo.setId(count.incrementAndGet());

        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehiculo() throws Exception {
        int databaseSizeBeforeUpdate = vehiculoRepository.findAll().size();
        vehiculo.setId(count.incrementAndGet());

        // Create the Vehiculo
        VehiculoDTO vehiculoDTO = vehiculoMapper.toDto(vehiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiculoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vehiculoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehiculo in the database
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehiculo() throws Exception {
        // Initialize the database
        vehiculoRepository.saveAndFlush(vehiculo);

        int databaseSizeBeforeDelete = vehiculoRepository.findAll().size();

        // Delete the vehiculo
        restVehiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehiculo> vehiculoList = vehiculoRepository.findAll();
        assertThat(vehiculoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
