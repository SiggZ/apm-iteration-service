package tum.sebis.apm.service;

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
     *  Delete the "id" sprintTeam.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}
