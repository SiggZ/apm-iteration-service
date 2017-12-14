package tum.sebis.apm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tum.sebis.apm.domain.Team;
import tum.sebis.apm.repository.TeamRepository;
import tum.sebis.apm.service.TeamService;
import tum.sebis.apm.web.rest.errors.TeamAlreadyExistsException;

import java.util.List;

/**
 * Service Implementation for managing Team.
 */
@Service
public class TeamServiceImpl implements TeamService{

    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Save a team.
     *
     * @param team the entity to save
     * @return the persisted entity
     */
    @Override
    public Team save(Team team) {
        log.debug("Request to save Team : {}", team);

        if (teamRepository.findByName(team.getName()).size() > 0) {
            throw new TeamAlreadyExistsException();
        }

        return teamRepository.save(team);
    }

    /**
     *  Get all the teams.
     *
     *  @return the list of entities
     */
    @Override
    public List<Team> findAll() {
        log.debug("Request to get all Teams");
        return teamRepository.findAll();
    }

    /**
     *  Get one team by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public Team findOne(String id) {
        log.debug("Request to get Team : {}", id);
        return teamRepository.findOne(id);
    }

    /**
     *  Delete the  team by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.delete(id);
    }
}
