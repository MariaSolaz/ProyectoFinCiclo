package es.florida.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import es.florida.IntegrationTest;
import es.florida.domain.Registro;
import es.florida.domain.Vehiculo;
import es.florida.repository.RegistroRepository;
import es.florida.service.criteria.RegistroCriteria;
import es.florida.service.dto.RegistroDTO;
import es.florida.service.mapper.RegistroMapper;
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
 * Integration tests for the {@link RegistroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegistroResourceIT {

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ESTADO_ACTUAL = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO_ACTUAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/registros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private RegistroMapper registroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegistroMockMvc;

    private Registro registro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registro createEntity(EntityManager em) {
        Registro registro = new Registro().fecha(DEFAULT_FECHA).estadoActual(DEFAULT_ESTADO_ACTUAL);
        return registro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registro createUpdatedEntity(EntityManager em) {
        Registro registro = new Registro().fecha(UPDATED_FECHA).estadoActual(UPDATED_ESTADO_ACTUAL);
        return registro;
    }

    @BeforeEach
    public void initTest() {
        registro = createEntity(em);
    }

    @Test
    @Transactional
    void createRegistro() throws Exception {
        int databaseSizeBeforeCreate = registroRepository.findAll().size();
        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);
        restRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isCreated());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeCreate + 1);
        Registro testRegistro = registroList.get(registroList.size() - 1);
        assertThat(testRegistro.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testRegistro.getEstadoActual()).isEqualTo(DEFAULT_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void createRegistroWithExistingId() throws Exception {
        // Create the Registro with an existing ID
        registro.setId(1L);
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        int databaseSizeBeforeCreate = registroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = registroRepository.findAll().size();
        // set the field null
        registro.setFecha(null);

        // Create the Registro, which fails.
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        restRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isBadRequest());

        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoActualIsRequired() throws Exception {
        int databaseSizeBeforeTest = registroRepository.findAll().size();
        // set the field null
        registro.setEstadoActual(null);

        // Create the Registro, which fails.
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        restRegistroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isBadRequest());

        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRegistros() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList
        restRegistroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registro.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estadoActual").value(hasItem(DEFAULT_ESTADO_ACTUAL)));
    }

    @Test
    @Transactional
    void getRegistro() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get the registro
        restRegistroMockMvc
            .perform(get(ENTITY_API_URL_ID, registro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registro.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.estadoActual").value(DEFAULT_ESTADO_ACTUAL));
    }

    @Test
    @Transactional
    void getRegistrosByIdFiltering() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        Long id = registro.getId();

        defaultRegistroShouldBeFound("id.equals=" + id);
        defaultRegistroShouldNotBeFound("id.notEquals=" + id);

        defaultRegistroShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegistroShouldNotBeFound("id.greaterThan=" + id);

        defaultRegistroShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegistroShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegistrosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where fecha equals to DEFAULT_FECHA
        defaultRegistroShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the registroList where fecha equals to UPDATED_FECHA
        defaultRegistroShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistrosByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where fecha not equals to DEFAULT_FECHA
        defaultRegistroShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the registroList where fecha not equals to UPDATED_FECHA
        defaultRegistroShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistrosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultRegistroShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the registroList where fecha equals to UPDATED_FECHA
        defaultRegistroShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistrosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where fecha is not null
        defaultRegistroShouldBeFound("fecha.specified=true");

        // Get all the registroList where fecha is null
        defaultRegistroShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistrosByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where fecha is greater than or equal to DEFAULT_FECHA
        defaultRegistroShouldBeFound("fecha.greaterThanOrEqual=" + DEFAULT_FECHA);

        // Get all the registroList where fecha is greater than or equal to UPDATED_FECHA
        defaultRegistroShouldNotBeFound("fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistrosByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where fecha is less than or equal to DEFAULT_FECHA
        defaultRegistroShouldBeFound("fecha.lessThanOrEqual=" + DEFAULT_FECHA);

        // Get all the registroList where fecha is less than or equal to SMALLER_FECHA
        defaultRegistroShouldNotBeFound("fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistrosByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where fecha is less than DEFAULT_FECHA
        defaultRegistroShouldNotBeFound("fecha.lessThan=" + DEFAULT_FECHA);

        // Get all the registroList where fecha is less than UPDATED_FECHA
        defaultRegistroShouldBeFound("fecha.lessThan=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistrosByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where fecha is greater than DEFAULT_FECHA
        defaultRegistroShouldNotBeFound("fecha.greaterThan=" + DEFAULT_FECHA);

        // Get all the registroList where fecha is greater than SMALLER_FECHA
        defaultRegistroShouldBeFound("fecha.greaterThan=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllRegistrosByEstadoActualIsEqualToSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where estadoActual equals to DEFAULT_ESTADO_ACTUAL
        defaultRegistroShouldBeFound("estadoActual.equals=" + DEFAULT_ESTADO_ACTUAL);

        // Get all the registroList where estadoActual equals to UPDATED_ESTADO_ACTUAL
        defaultRegistroShouldNotBeFound("estadoActual.equals=" + UPDATED_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void getAllRegistrosByEstadoActualIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where estadoActual not equals to DEFAULT_ESTADO_ACTUAL
        defaultRegistroShouldNotBeFound("estadoActual.notEquals=" + DEFAULT_ESTADO_ACTUAL);

        // Get all the registroList where estadoActual not equals to UPDATED_ESTADO_ACTUAL
        defaultRegistroShouldBeFound("estadoActual.notEquals=" + UPDATED_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void getAllRegistrosByEstadoActualIsInShouldWork() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where estadoActual in DEFAULT_ESTADO_ACTUAL or UPDATED_ESTADO_ACTUAL
        defaultRegistroShouldBeFound("estadoActual.in=" + DEFAULT_ESTADO_ACTUAL + "," + UPDATED_ESTADO_ACTUAL);

        // Get all the registroList where estadoActual equals to UPDATED_ESTADO_ACTUAL
        defaultRegistroShouldNotBeFound("estadoActual.in=" + UPDATED_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void getAllRegistrosByEstadoActualIsNullOrNotNull() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where estadoActual is not null
        defaultRegistroShouldBeFound("estadoActual.specified=true");

        // Get all the registroList where estadoActual is null
        defaultRegistroShouldNotBeFound("estadoActual.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistrosByEstadoActualContainsSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where estadoActual contains DEFAULT_ESTADO_ACTUAL
        defaultRegistroShouldBeFound("estadoActual.contains=" + DEFAULT_ESTADO_ACTUAL);

        // Get all the registroList where estadoActual contains UPDATED_ESTADO_ACTUAL
        defaultRegistroShouldNotBeFound("estadoActual.contains=" + UPDATED_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void getAllRegistrosByEstadoActualNotContainsSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        // Get all the registroList where estadoActual does not contain DEFAULT_ESTADO_ACTUAL
        defaultRegistroShouldNotBeFound("estadoActual.doesNotContain=" + DEFAULT_ESTADO_ACTUAL);

        // Get all the registroList where estadoActual does not contain UPDATED_ESTADO_ACTUAL
        defaultRegistroShouldBeFound("estadoActual.doesNotContain=" + UPDATED_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void getAllRegistrosByVehiculoIsEqualToSomething() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);
        Vehiculo vehiculo = VehiculoResourceIT.createEntity(em);
        em.persist(vehiculo);
        em.flush();
        registro.setVehiculo(vehiculo);
        registroRepository.saveAndFlush(registro);
        Long vehiculoId = vehiculo.getId();

        // Get all the registroList where vehiculo equals to vehiculoId
        defaultRegistroShouldBeFound("vehiculoId.equals=" + vehiculoId);

        // Get all the registroList where vehiculo equals to (vehiculoId + 1)
        defaultRegistroShouldNotBeFound("vehiculoId.equals=" + (vehiculoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegistroShouldBeFound(String filter) throws Exception {
        restRegistroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registro.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].estadoActual").value(hasItem(DEFAULT_ESTADO_ACTUAL)));

        // Check, that the count call also returns 1
        restRegistroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegistroShouldNotBeFound(String filter) throws Exception {
        restRegistroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegistroMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRegistro() throws Exception {
        // Get the registro
        restRegistroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRegistro() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        int databaseSizeBeforeUpdate = registroRepository.findAll().size();

        // Update the registro
        Registro updatedRegistro = registroRepository.findById(registro.getId()).get();
        // Disconnect from session so that the updates on updatedRegistro are not directly saved in db
        em.detach(updatedRegistro);
        updatedRegistro.fecha(UPDATED_FECHA).estadoActual(UPDATED_ESTADO_ACTUAL);
        RegistroDTO registroDTO = registroMapper.toDto(updatedRegistro);

        restRegistroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
        Registro testRegistro = registroList.get(registroList.size() - 1);
        assertThat(testRegistro.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRegistro.getEstadoActual()).isEqualTo(UPDATED_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void putNonExistingRegistro() throws Exception {
        int databaseSizeBeforeUpdate = registroRepository.findAll().size();
        registro.setId(count.incrementAndGet());

        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegistro() throws Exception {
        int databaseSizeBeforeUpdate = registroRepository.findAll().size();
        registro.setId(count.incrementAndGet());

        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegistro() throws Exception {
        int databaseSizeBeforeUpdate = registroRepository.findAll().size();
        registro.setId(count.incrementAndGet());

        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(registroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegistroWithPatch() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        int databaseSizeBeforeUpdate = registroRepository.findAll().size();

        // Update the registro using partial update
        Registro partialUpdatedRegistro = new Registro();
        partialUpdatedRegistro.setId(registro.getId());

        partialUpdatedRegistro.fecha(UPDATED_FECHA);

        restRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistro))
            )
            .andExpect(status().isOk());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
        Registro testRegistro = registroList.get(registroList.size() - 1);
        assertThat(testRegistro.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRegistro.getEstadoActual()).isEqualTo(DEFAULT_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void fullUpdateRegistroWithPatch() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        int databaseSizeBeforeUpdate = registroRepository.findAll().size();

        // Update the registro using partial update
        Registro partialUpdatedRegistro = new Registro();
        partialUpdatedRegistro.setId(registro.getId());

        partialUpdatedRegistro.fecha(UPDATED_FECHA).estadoActual(UPDATED_ESTADO_ACTUAL);

        restRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegistro))
            )
            .andExpect(status().isOk());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
        Registro testRegistro = registroList.get(registroList.size() - 1);
        assertThat(testRegistro.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testRegistro.getEstadoActual()).isEqualTo(UPDATED_ESTADO_ACTUAL);
    }

    @Test
    @Transactional
    void patchNonExistingRegistro() throws Exception {
        int databaseSizeBeforeUpdate = registroRepository.findAll().size();
        registro.setId(count.incrementAndGet());

        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, registroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegistro() throws Exception {
        int databaseSizeBeforeUpdate = registroRepository.findAll().size();
        registro.setId(count.incrementAndGet());

        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(registroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegistro() throws Exception {
        int databaseSizeBeforeUpdate = registroRepository.findAll().size();
        registro.setId(count.incrementAndGet());

        // Create the Registro
        RegistroDTO registroDTO = registroMapper.toDto(registro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(registroDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Registro in the database
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegistro() throws Exception {
        // Initialize the database
        registroRepository.saveAndFlush(registro);

        int databaseSizeBeforeDelete = registroRepository.findAll().size();

        // Delete the registro
        restRegistroMockMvc
            .perform(delete(ENTITY_API_URL_ID, registro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Registro> registroList = registroRepository.findAll();
        assertThat(registroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
