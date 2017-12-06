package tum.sebis.apm.web.rest;

import tum.sebis.apm.IterationServiceApp;

import tum.sebis.apm.domain.SprintTeam;
import tum.sebis.apm.repository.SprintTeamRepository;
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

import java.util.List;

import static tum.sebis.apm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SprintTeamResource REST controller.
 *
 * @see SprintTeamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IterationServiceApp.class)
public class SprintTeamResourceIntTest {

    private static final String DEFAULT_TEAM_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEAM_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SPRINT_ID = "AAAAAAAAAA";
    private static final String UPDATED_SPRINT_ID = "BBBBBBBBBB";

    @Autowired
    private SprintTeamRepository sprintTeamRepository;

    @Autowired
    private SprintTeamService sprintTeamService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restSprintTeamMockMvc;

    private SprintTeam sprintTeam;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SprintTeamResource sprintTeamResource = new SprintTeamResource(sprintTeamService);
        this.restSprintTeamMockMvc = MockMvcBuilders.standaloneSetup(sprintTeamResource)
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
    public static SprintTeam createEntity() {
        SprintTeam sprintTeam = new SprintTeam()
            .teamId(DEFAULT_TEAM_ID)
            .sprintId(DEFAULT_SPRINT_ID);
        return sprintTeam;
    }

    @Before
    public void initTest() {
        sprintTeamRepository.deleteAll();
        sprintTeam = createEntity();
    }

    @Test
    public void createSprintTeam() throws Exception {
        int databaseSizeBeforeCreate = sprintTeamRepository.findAll().size();

        // Create the SprintTeam
        restSprintTeamMockMvc.perform(post("/api/sprint-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintTeam)))
            .andExpect(status().isCreated());

        // Validate the SprintTeam in the database
        List<SprintTeam> sprintTeamList = sprintTeamRepository.findAll();
        assertThat(sprintTeamList).hasSize(databaseSizeBeforeCreate + 1);
        SprintTeam testSprintTeam = sprintTeamList.get(sprintTeamList.size() - 1);
        assertThat(testSprintTeam.getTeamId()).isEqualTo(DEFAULT_TEAM_ID);
        assertThat(testSprintTeam.getSprintId()).isEqualTo(DEFAULT_SPRINT_ID);
    }

    @Test
    public void createSprintTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sprintTeamRepository.findAll().size();

        // Create the SprintTeam with an existing ID
        sprintTeam.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintTeamMockMvc.perform(post("/api/sprint-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintTeam)))
            .andExpect(status().isBadRequest());

        // Validate the SprintTeam in the database
        List<SprintTeam> sprintTeamList = sprintTeamRepository.findAll();
        assertThat(sprintTeamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkTeamIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintTeamRepository.findAll().size();
        // set the field null
        sprintTeam.setTeamId(null);

        // Create the SprintTeam, which fails.

        restSprintTeamMockMvc.perform(post("/api/sprint-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintTeam)))
            .andExpect(status().isBadRequest());

        List<SprintTeam> sprintTeamList = sprintTeamRepository.findAll();
        assertThat(sprintTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSprintIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintTeamRepository.findAll().size();
        // set the field null
        sprintTeam.setSprintId(null);

        // Create the SprintTeam, which fails.

        restSprintTeamMockMvc.perform(post("/api/sprint-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintTeam)))
            .andExpect(status().isBadRequest());

        List<SprintTeam> sprintTeamList = sprintTeamRepository.findAll();
        assertThat(sprintTeamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSprintTeams() throws Exception {
        // Initialize the database
        sprintTeamRepository.save(sprintTeam);

        // Get all the sprintTeamList
        restSprintTeamMockMvc.perform(get("/api/sprint-teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprintTeam.getId())))
            .andExpect(jsonPath("$.[*].teamId").value(hasItem(DEFAULT_TEAM_ID.toString())))
            .andExpect(jsonPath("$.[*].sprintId").value(hasItem(DEFAULT_SPRINT_ID.toString())));
    }

    @Test
    public void getSprintTeam() throws Exception {
        // Initialize the database
        sprintTeamRepository.save(sprintTeam);

        // Get the sprintTeam
        restSprintTeamMockMvc.perform(get("/api/sprint-teams/{id}", sprintTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sprintTeam.getId()))
            .andExpect(jsonPath("$.teamId").value(DEFAULT_TEAM_ID.toString()))
            .andExpect(jsonPath("$.sprintId").value(DEFAULT_SPRINT_ID.toString()));
    }

    @Test
    public void getNonExistingSprintTeam() throws Exception {
        // Get the sprintTeam
        restSprintTeamMockMvc.perform(get("/api/sprint-teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSprintTeam() throws Exception {
        // Initialize the database
        sprintTeamService.save(sprintTeam);

        int databaseSizeBeforeUpdate = sprintTeamRepository.findAll().size();

        // Update the sprintTeam
        SprintTeam updatedSprintTeam = sprintTeamRepository.findOne(sprintTeam.getId());
        updatedSprintTeam
            .teamId(UPDATED_TEAM_ID)
            .sprintId(UPDATED_SPRINT_ID);

        restSprintTeamMockMvc.perform(put("/api/sprint-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSprintTeam)))
            .andExpect(status().isOk());

        // Validate the SprintTeam in the database
        List<SprintTeam> sprintTeamList = sprintTeamRepository.findAll();
        assertThat(sprintTeamList).hasSize(databaseSizeBeforeUpdate);
        SprintTeam testSprintTeam = sprintTeamList.get(sprintTeamList.size() - 1);
        assertThat(testSprintTeam.getTeamId()).isEqualTo(UPDATED_TEAM_ID);
        assertThat(testSprintTeam.getSprintId()).isEqualTo(UPDATED_SPRINT_ID);
    }

    @Test
    public void updateNonExistingSprintTeam() throws Exception {
        int databaseSizeBeforeUpdate = sprintTeamRepository.findAll().size();

        // Create the SprintTeam

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSprintTeamMockMvc.perform(put("/api/sprint-teams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sprintTeam)))
            .andExpect(status().isCreated());

        // Validate the SprintTeam in the database
        List<SprintTeam> sprintTeamList = sprintTeamRepository.findAll();
        assertThat(sprintTeamList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteSprintTeam() throws Exception {
        // Initialize the database
        sprintTeamService.save(sprintTeam);

        int databaseSizeBeforeDelete = sprintTeamRepository.findAll().size();

        // Get the sprintTeam
        restSprintTeamMockMvc.perform(delete("/api/sprint-teams/{id}", sprintTeam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SprintTeam> sprintTeamList = sprintTeamRepository.findAll();
        assertThat(sprintTeamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SprintTeam.class);
        SprintTeam sprintTeam1 = new SprintTeam();
        sprintTeam1.setId("id1");
        SprintTeam sprintTeam2 = new SprintTeam();
        sprintTeam2.setId(sprintTeam1.getId());
        assertThat(sprintTeam1).isEqualTo(sprintTeam2);
        sprintTeam2.setId("id2");
        assertThat(sprintTeam1).isNotEqualTo(sprintTeam2);
        sprintTeam1.setId(null);
        assertThat(sprintTeam1).isNotEqualTo(sprintTeam2);
    }
}
