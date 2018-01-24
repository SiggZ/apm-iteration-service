package tum.sebis.apm.service;

import tum.sebis.apm.domain.Team;
import java.util.List;

/**
 * Service Interface for managing Team.
 */
public interface TeamService {

	/**
	 * Save a team.
	 *
	 * @param team
	 *            the entity to save
	 * @return the persisted entity
	 */
	Team save(Team team);

	/**
	 * Get all the teams.
	 *
	 * @return the list of entities
	 */
	List<Team> findAll();

	/**
	 * Get the "id" team.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	Team findOne(String id);

	/**
	 * Delete the "id" team.
	 *
	 * @param id
	 *            the id of the entity
	 */
	void delete(String id);
}
