package tum.sebis.apm.service;

import tum.sebis.apm.domain.Iteration;
import tum.sebis.apm.domain.SprintTeam;

import java.util.List;

/**
 * Service Interface for managing SprintTeam.
 */
public interface SprintTeamService {

    /**
     * Save a sprintTeam.
     *
     * @param sprintTeam the entity to save
     * @return the persisted entity
     */
    SprintTeam save(SprintTeam sprintTeam);

    /**
     *  Get all the sprintTeams.
     *
     *  @return the list of entities
     */
    List<SprintTeam> findAll();

    /**
     *  Get the "id" sprintTeam.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SprintTeam findOne(String id);

    /**
     *  Get all the sprintTeams of a given sprint.
     *
     *  @param sprint
     *  @return the list of entities
     */
    List<SprintTeam> findBySprint(Iteration sprint);

    /**
     *  Delete the "id" sprintTeam.
     *
     *  @param id the id of the entity
     */
    void delete(String id);

    /**
     *  Delete all sprintTeams of a given sprint.
     *
     *  @param sprint the sprint
     */
    void deleteForSprint(Iteration sprint);

    /**
     *  Calculate the capacity for the SprintTeam with the given id
     *
     * @param sprintTeamId the id of the sprint team for which to calculate the capacity
     * @return the calculated capacity
     */
    double calculateCapacity(String sprintTeamId);

    /**
     *  Calculate the velocity for the SprintTeam with the given id
     *
     * @param sprintTeamId the id of the sprint team for which to calculate the velocity
     * @return the calculated velocity
     */
    double calculateVelocity(String sprintTeamId);
}
