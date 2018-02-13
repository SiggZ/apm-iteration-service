package tum.sebis.apm.web.rest;

import tum.sebis.apm.IterationServiceApp;

import tum.sebis.apm.domain.Iteration;
import tum.sebis.apm.repository.IterationRepository;
import tum.sebis.apm.service.IterationService;
import tum.sebis.apm.service.SprintTeamService;
import tum.sebis.apm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static tum.sebis.apm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IterationResource REST controller.
 *
 * @see IterationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IterationServiceApp.class)
public class IterationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private IterationRepository iterationRepository;

    @Autowired
    private IterationService iterationService;

    @Autowired
    private SprintTeamService sprintTeamService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restIterationMockMvc;

    private Iteration iteration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IterationResource iterationResource = new IterationResource(iterationService, sprintTeamService);
        this.restIterationMockMvc = MockMvcBuilders.standaloneSetup(iterationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Iteration createEntity() {
        Iteration iteration = new Iteration()
            .name(DEFAULT_NAME)
            .start(DEFAULT_START)
            .end(DEFAULT_END);
        return iteration;
    }

    @Before
    public void initTest() {
        iterationRepository.deleteAll();
        iteration = createEntity();
    }

    @Test
    public void createIteration() throws Exception {
        int databaseSizeBeforeCreate = iterationRepository.findAll().size();

        // Create the Iteration
        restIterationMockMvc.perform(post("/api/iterations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iteration)))
            .andExpect(status().isCreated());

        // Validate the Iteration in the database
        List<Iteration> iterationList = iterationRepository.findAll();
        assertThat(iterationList).hasSize(databaseSizeBeforeCreate + 1);
        Iteration testIteration = iterationList.get(iterationList.size() - 1);
        assertThat(testIteration.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIteration.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testIteration.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    public void createIterationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = iterationRepository.findAll().size();

        // Create the Iteration with an existing ID
        iteration.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restIterationMockMvc.perform(post("/api/iterations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iteration)))
            .andExpect(status().isBadRequest());

        // Validate the Iteration in the database
        List<Iteration> iterationList = iterationRepository.findAll();
        assertThat(iterationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = iterationRepository.findAll().size();
        // set the field null
        iteration.setName(null);

        // Create the Iteration, which fails.

        restIterationMockMvc.perform(post("/api/iterations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iteration)))
            .andExpect(status().isBadRequest());

        List<Iteration> iterationList = iterationRepository.findAll();
        assertThat(iterationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllIterations() throws Exception {
        // Initialize the database
        iterationRepository.save(iteration);

        // Get all the iterationList
        restIterationMockMvc.perform(get("/api/iterations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(iteration.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }

    @Test
    public void getIteration() throws Exception {
        // Initialize the database
        iterationRepository.save(iteration);

        // Get the iteration
        restIterationMockMvc.perform(get("/api/iterations/{id}", iteration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(iteration.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()));
    }

    @Test
    public void getNonExistingIteration() throws Exception {
        // Get the iteration
        restIterationMockMvc.perform(get("/api/iterations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateIteration() throws Exception {
        // Initialize the database
        iterationService.save(iteration);

        int databaseSizeBeforeUpdate = iterationRepository.findAll().size();

        // Update the iteration
        Iteration updatedIteration = iterationRepository.findOne(iteration.getId());
        updatedIteration
            .name(UPDATED_NAME)
            .start(UPDATED_START)
            .end(UPDATED_END);

        restIterationMockMvc.perform(put("/api/iterations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIteration)))
            .andExpect(status().isOk());

        // Validate the Iteration in the database
        List<Iteration> iterationList = iterationRepository.findAll();
        assertThat(iterationList).hasSize(databaseSizeBeforeUpdate);
        Iteration testIteration = iterationList.get(iterationList.size() - 1);
        assertThat(testIteration.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIteration.getStart()).isEqualTo(UPDATED_START);
        assertThat(testIteration.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    public void updateNonExistingIteration() throws Exception {
        int databaseSizeBeforeUpdate = iterationRepository.findAll().size();

        // Create the Iteration

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIterationMockMvc.perform(put("/api/iterations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iteration)))
            .andExpect(status().isCreated());

        // Validate the Iteration in the database
        List<Iteration> iterationList = iterationRepository.findAll();
        assertThat(iterationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteIteration() throws Exception {
        // Initialize the database
        iterationService.save(iteration);

        int databaseSizeBeforeDelete = iterationRepository.findAll().size();

        // Get the iteration
        restIterationMockMvc.perform(delete("/api/iterations/{id}", iteration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Iteration> iterationList = iterationRepository.findAll();
        assertThat(iterationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Iteration.class);
        Iteration iteration1 = new Iteration();
        iteration1.setId("id1");
        Iteration iteration2 = new Iteration();
        iteration2.setId(iteration1.getId());
        assertThat(iteration1).isEqualTo(iteration2);
        iteration2.setId("id2");
        assertThat(iteration1).isNotEqualTo(iteration2);
        iteration1.setId(null);
        assertThat(iteration1).isNotEqualTo(iteration2);
    }
}
