package tum.sebis.apm.service;

import tum.sebis.apm.domain.Iteration;
import java.util.List;
import java.time.LocalDate;

/**
 * Service Interface for managing Iteration.
 */
public interface IterationService {

    /**
     * Save a iteration.
     *
     * @param iteration the entity to save
     * @return the persisted entity
     */
    Iteration save(Iteration iteration);

    /**
     * Get all the iterations.
     *
     * @return the list of entities
     */
    List<Iteration> findAll();

    /**
     * Get the "id" iteration.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Iteration findOne(String id);

    /**
     * Delete the "id" iteration.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Get all work days from start to end date for sprint.
     *
     * @return the list of dates
     */
    List<LocalDate> getListOfDaysForSprint(String sprintTeamId);
}
