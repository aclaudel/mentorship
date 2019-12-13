package aclaudel.codurance.web.rest;

import aclaudel.codurance.MentorshipApp;
import aclaudel.codurance.domain.Craftsperson;
import aclaudel.codurance.repository.CraftspersonRepository;
import aclaudel.codurance.service.CraftspersonService;
import aclaudel.codurance.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static aclaudel.codurance.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CraftspersonResource} REST controller.
 */
@SpringBootTest(classes = MentorshipApp.class)
public class CraftspersonResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private CraftspersonRepository craftspersonRepository;

    @Autowired
    private CraftspersonService craftspersonService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCraftspersonMockMvc;

    private Craftsperson craftsperson;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CraftspersonResource craftspersonResource = new CraftspersonResource(craftspersonService);
        this.restCraftspersonMockMvc = MockMvcBuilders.standaloneSetup(craftspersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Craftsperson createEntity(EntityManager em) {
        Craftsperson craftsperson = new Craftsperson()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL);
        return craftsperson;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Craftsperson createUpdatedEntity(EntityManager em) {
        Craftsperson craftsperson = new Craftsperson()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);
        return craftsperson;
    }

    @BeforeEach
    public void initTest() {
        craftsperson = createEntity(em);
    }

    @Test
    @Transactional
    public void createCraftsperson() throws Exception {
        int databaseSizeBeforeCreate = craftspersonRepository.findAll().size();

        // Create the Craftsperson
        restCraftspersonMockMvc.perform(post("/api/craftspeople")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(craftsperson)))
            .andExpect(status().isCreated());

        // Validate the Craftsperson in the database
        List<Craftsperson> craftspersonList = craftspersonRepository.findAll();
        assertThat(craftspersonList).hasSize(databaseSizeBeforeCreate + 1);
        Craftsperson testCraftsperson = craftspersonList.get(craftspersonList.size() - 1);
        assertThat(testCraftsperson.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCraftsperson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCraftsperson.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createCraftspersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = craftspersonRepository.findAll().size();

        // Create the Craftsperson with an existing ID
        craftsperson.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCraftspersonMockMvc.perform(post("/api/craftspeople")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(craftsperson)))
            .andExpect(status().isBadRequest());

        // Validate the Craftsperson in the database
        List<Craftsperson> craftspersonList = craftspersonRepository.findAll();
        assertThat(craftspersonList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCraftspeople() throws Exception {
        // Initialize the database
        craftspersonRepository.saveAndFlush(craftsperson);

        // Get all the craftspersonList
        restCraftspersonMockMvc.perform(get("/api/craftspeople?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(craftsperson.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getCraftsperson() throws Exception {
        // Initialize the database
        craftspersonRepository.saveAndFlush(craftsperson);

        // Get the craftsperson
        restCraftspersonMockMvc.perform(get("/api/craftspeople/{id}", craftsperson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(craftsperson.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    public void getNonExistingCraftsperson() throws Exception {
        // Get the craftsperson
        restCraftspersonMockMvc.perform(get("/api/craftspeople/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCraftsperson() throws Exception {
        // Initialize the database
        craftspersonService.save(craftsperson);

        int databaseSizeBeforeUpdate = craftspersonRepository.findAll().size();

        // Update the craftsperson
        Craftsperson updatedCraftsperson = craftspersonRepository.findById(craftsperson.getId()).get();
        // Disconnect from session so that the updates on updatedCraftsperson are not directly saved in db
        em.detach(updatedCraftsperson);
        updatedCraftsperson
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);

        restCraftspersonMockMvc.perform(put("/api/craftspeople")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCraftsperson)))
            .andExpect(status().isOk());

        // Validate the Craftsperson in the database
        List<Craftsperson> craftspersonList = craftspersonRepository.findAll();
        assertThat(craftspersonList).hasSize(databaseSizeBeforeUpdate);
        Craftsperson testCraftsperson = craftspersonList.get(craftspersonList.size() - 1);
        assertThat(testCraftsperson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCraftsperson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCraftsperson.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingCraftsperson() throws Exception {
        int databaseSizeBeforeUpdate = craftspersonRepository.findAll().size();

        // Create the Craftsperson

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCraftspersonMockMvc.perform(put("/api/craftspeople")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(craftsperson)))
            .andExpect(status().isBadRequest());

        // Validate the Craftsperson in the database
        List<Craftsperson> craftspersonList = craftspersonRepository.findAll();
        assertThat(craftspersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCraftsperson() throws Exception {
        // Initialize the database
        craftspersonService.save(craftsperson);

        int databaseSizeBeforeDelete = craftspersonRepository.findAll().size();

        // Delete the craftsperson
        restCraftspersonMockMvc.perform(delete("/api/craftspeople/{id}", craftsperson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Craftsperson> craftspersonList = craftspersonRepository.findAll();
        assertThat(craftspersonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
