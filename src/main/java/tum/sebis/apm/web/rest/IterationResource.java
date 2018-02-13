package tum.sebis.apm.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tum.sebis.apm.domain.Iteration;
import tum.sebis.apm.service.IterationService;
import tum.sebis.apm.service.SprintTeamService;
import tum.sebis.apm.web.rest.errors.BadRequestAlertException;
import tum.sebis.apm.web.rest.errors.SprintNotFoundException;
import tum.sebis.apm.web.rest.util.HeaderUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Iteration.
 */
@RestController
@RequestMapping("/api")
public class IterationResource {

    private final Logger log = LoggerFactory.getLogger(IterationResource.class);

    private static final String ENTITY_NAME = "iteration";

    private final IterationService iterationService;

    private final SprintTeamService sprintTeamService;

    public IterationResource(IterationService iterationService, SprintTeamService sprintTeamService) {
        this.iterationService = iterationService;
        this.sprintTeamService = sprintTeamService;
    }

    /**
     * POST  /iterations : Create a new iteration.
     *
     * @param iteration the iteration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new iteration, or with status 400 (Bad Request) if the iteration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/iterations")
    @Timed
    public ResponseEntity<Iteration> createIteration(@Valid @RequestBody Iteration iteration) throws URISyntaxException {
        log.debug("REST request to save Iteration : {}", iteration);
        if (iteration.getId() != null) {
            throw new BadRequestAlertException("A new iteration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Iteration result = iterationService.save(iteration);
        return ResponseEntity.created(new URI("/api/iterations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /iterations : Updates an existing iteration and also updates the available days of all people in the sprint.
     *
     * @param iteration the iteration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated iteration,
     * or with status 400 (Bad Request) if the iteration is not valid,
     * or with status 500 (Internal Server Error) if the iteration couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/iterations")
    @Timed
    public ResponseEntity<Iteration> updateIteration(@Valid @RequestBody Iteration iteration) throws URISyntaxException {
        log.debug("REST request to update Iteration : {}", iteration);
        if (iteration.getId() == null) {
            return createIteration(iteration);
        }
        Iteration sprintBeforeUpdate = iterationService.findOne(iteration.getId());
        Iteration result = iterationService.save(iteration);
        if (sprintBeforeUpdate != null && datesChanged(sprintBeforeUpdate, result)) {
            sprintTeamService.updateAvailableDays(result);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, iteration.getId().toString()))
            .body(result);
    }

    /**
     *  Determines if either one or both dates of a sprint changed after updating it
     *
     * @param beforeUpdate the sprint before the update
     * @param afterUpdate the sprint after the update
     * @return true, if at least one date changed, false otherwise
     */
    private boolean datesChanged(Iteration beforeUpdate, Iteration afterUpdate) {
        return !beforeUpdate.getStart().equals(afterUpdate.getStart())
            || !beforeUpdate.getEnd().equals(afterUpdate.getEnd());
    }

    /**
     * GET  /iterations : get all the iterations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of iterations in body
     */
    @GetMapping("/iterations")
    @Timed
    public List<Iteration> getAllIterations() {
        log.debug("REST request to get all Iterations");
        return iterationService.findAll();
        }

    /**
     * GET  /iterations/:id : get the "id" iteration.
     *
     * @param id the id of the iteration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the iteration, or with status 404 (Not Found)
     */
    @GetMapping("/iterations/{id}")
    @Timed
    public ResponseEntity<Iteration> getIteration(@PathVariable String id) {
        log.debug("REST request to get Iteration : {}", id);
        Iteration iteration = iterationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(iteration));
    }

    /**
     * DELETE  /iterations/:id : delete the "id" iteration and all its sprintTeams.
     *
     * @param id the id of the iteration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/iterations/{id}")
    @Timed
    public ResponseEntity<Void> deleteIteration(@PathVariable String id) {
        log.debug("REST request to delete Iteration : {}", id);
        Iteration sprint = iterationService.findOne(id);
        if (sprint == null){
            throw new SprintNotFoundException();
        }
        sprintTeamService.deleteForSprint(sprint);
        iterationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }


    /**
     * GET  /iterations/:id/dayslist : gets the capacity for sprintTeam with given id.
     *
     * @param id the id of the iteration to retrieve the list of days for
     * @return List of dates or an empty list.
     */
    @GetMapping("/iterations/{id}/dayslist")
    @Timed
    public List<LocalDate> getListOfDaysForSprint(@PathVariable String id) {
        log.debug("REST request to get the available days in a sprint: {}", id);
        return iterationService.getListOfDaysForSprint(id);
    }
}
