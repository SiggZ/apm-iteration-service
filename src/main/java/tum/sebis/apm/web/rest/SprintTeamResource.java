package tum.sebis.apm.web.rest;

import com.codahale.metrics.annotation.Timed;

import tum.sebis.apm.domain.Iteration;
import tum.sebis.apm.domain.SprintTeam;
import tum.sebis.apm.service.SprintTeamService;
import tum.sebis.apm.service.IterationService;
import tum.sebis.apm.repository.SprintTeamRepository;
import tum.sebis.apm.web.rest.errors.BadRequestAlertException;
import tum.sebis.apm.web.rest.errors.SprintNotFoundException;
import tum.sebis.apm.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SprintTeam.
 */
@RestController
@RequestMapping("/api")
public class SprintTeamResource {

    private final Logger log = LoggerFactory.getLogger(SprintTeamResource.class);

    private static final String ENTITY_NAME = "sprintTeam";

    private final SprintTeamService sprintTeamService;

    private final IterationService iterationService;

    private final SprintTeamRepository sprintTeamRepository;

    public SprintTeamResource(SprintTeamService sprintTeamService, IterationService iterationService, SprintTeamRepository sprintTeamRepository ) {
        this.sprintTeamService = sprintTeamService;
        this.iterationService = iterationService;
        this.sprintTeamRepository = sprintTeamRepository;
    }

    /**
     * POST  /sprint-teams : Create a new sprintTeam.
     *
     * @param sprintTeam the sprintTeam to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sprintTeam, or with status 400 (Bad Request) if the sprintTeam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sprint-teams")
    @Timed
    public ResponseEntity<SprintTeam> createSprintTeam(@Valid @RequestBody SprintTeam sprintTeam) throws URISyntaxException {
        log.debug("REST request to save SprintTeam : {}", sprintTeam);
        if (sprintTeam.getId() != null) {
            throw new BadRequestAlertException("A new sprintTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }

        SprintTeam result = sprintTeamService.save(sprintTeam);
        return ResponseEntity.created(new URI("/api/sprint-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sprint-teams : Updates an existing sprintTeam.
     *
     * @param sprintTeam the sprintTeam to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sprintTeam,
     * or with status 400 (Bad Request) if the sprintTeam is not valid,
     * or with status 500 (Internal Server Error) if the sprintTeam couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sprint-teams")
    @Timed
    public ResponseEntity<SprintTeam> updateSprintTeam(@Valid @RequestBody SprintTeam sprintTeam) throws URISyntaxException {
        log.debug("REST request to update SprintTeam : {}", sprintTeam);
        if (sprintTeam.getId() == null) {
            return createSprintTeam(sprintTeam);
        }

        SprintTeam result = sprintTeamService.save(sprintTeam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sprintTeam.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sprint-teams : get all the sprintTeams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sprintTeams in body
     */
    @GetMapping("/sprint-teams")
    @Timed
    public List<SprintTeam> getAllSprintTeams() {
        log.debug("REST request to get all SprintTeams");
        return sprintTeamService.findAll();
        }

    /**
     * GET  /sprint-teams/:id : get the "id" sprintTeam.
     *
     * @param id the id of the sprintTeam to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sprintTeam, or with status 404 (Not Found)
     */
    @GetMapping("/sprint-teams/{id}")
    @Timed
    public ResponseEntity<SprintTeam> getSprintTeam(@PathVariable String id) {
        log.debug("REST request to get SprintTeam : {}", id);
        SprintTeam sprintTeam = sprintTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sprintTeam));
    }

    /**
     * GET  /sprint-teams-by-sprint/:sprintId : get all the sprint teams for a particular sprint.
     *
     * @param sprintId the id of the sprint
     * @return the list of sprint teams for the given sprint id
     */
    @GetMapping("/sprint-teams-by-sprint/{sprintId}")
    @Timed
    public List<SprintTeam> getSprintTeamsBySprint(@PathVariable String sprintId) {
        log.debug("REST request to get Sprint Teams by Sprint: {}", sprintId);
        Iteration sprint = iterationService.findOne(sprintId);
        if (sprint == null) {
            throw new SprintNotFoundException();
        }
        return sprintTeamService.findBySprint(sprint);
    }

    /**
     * DELETE  /sprint-teams/:id : delete the "id" sprintTeam.
     *
     * @param id the id of the sprintTeam to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sprint-teams/{id}")
    @Timed
    public ResponseEntity<Void> deleteSprintTeam(@PathVariable String id) {
        log.debug("REST request to delete SprintTeam : {}", id);
        sprintTeamService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * GET  /sprint-teams/:id/capacity : gets the capacity for sprintTeam with given id.
     *
     * @param id the id of the sprintTeam to retrieve the capacity for
     * @return the ResponseEntity with status 200 (OK) and the capacity in the body, or with status 404 (Not Found)
     */
    @GetMapping("/sprint-teams/{id}/capacity")
    @Timed
    public ResponseEntity<Double> getSprintTeamCapacity(@PathVariable String id) {
        log.debug("REST request to get capacity for SprintTeam : {}", id);
        double capacity = sprintTeamService.calculateCapacity(id);
        return ResponseEntity.ok(capacity);
    }

    /**
     * GET  /sprint-teams/:id/velocity : gets the velocity for sprintTeam with given id.
     *
     * @param id the id of the sprintTeam to retrieve the velocity for
     * @return the ResponseEntity with status 200 (OK) and the velocity in the body, or with status 404 (Not Found)
     */
    @GetMapping("/sprint-teams/{id}/velocity")
    @Timed
    public ResponseEntity<Double> getSprintTeamVelocity(@PathVariable String id) {
        log.debug("REST request to get velocity for SprintTeam : {}", id);
        double velocity = sprintTeamService.calculateVelocity(id);
        return ResponseEntity.ok(velocity);
    }
}
