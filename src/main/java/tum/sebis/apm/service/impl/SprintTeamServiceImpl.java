package tum.sebis.apm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tum.sebis.apm.domain.SprintTeam;
import tum.sebis.apm.repository.SprintTeamRepository;
import tum.sebis.apm.service.IterationService;
import tum.sebis.apm.service.SprintTeamService;
import tum.sebis.apm.service.TeamService;
import tum.sebis.apm.web.rest.errors.IterationNotFoundException;
import tum.sebis.apm.web.rest.errors.SprintTeamNotFoundException;
import tum.sebis.apm.web.rest.errors.TeamAlreadyInSprintException;
import tum.sebis.apm.web.rest.errors.TeamNotFoundException;

import java.util.List;

/**
 * Service Implementation for managing SprintTeam.
 */
@Service
public class SprintTeamServiceImpl implements SprintTeamService{

    private final Logger log = LoggerFactory.getLogger(SprintTeamServiceImpl.class);

    private final SprintTeamRepository sprintTeamRepository;
    private final IterationService iterationService;
    private final TeamService teamService;

    public SprintTeamServiceImpl(SprintTeamRepository sprintTeamRepository, IterationService iterationService, TeamService teamService) {
        this.sprintTeamRepository = sprintTeamRepository;
        this.iterationService = iterationService;
        this.teamService = teamService;
    }

    /**
     * Save a sprintTeam.
     *
     * @param sprintTeam the entity to save
     * @return the persisted entity
     */
    @Override
    public SprintTeam save(SprintTeam sprintTeam) {
        log.debug("Request to save SprintTeam : {}", sprintTeam);

        if (sprintTeamRepository.findBySprintAndTeam(sprintTeam.getSprint(), sprintTeam.getTeam()).size() > 0) {
            throw new TeamAlreadyInSprintException();
        }
        if (iterationService.findOne(sprintTeam.getSprint().getId()) == null) {
            throw new IterationNotFoundException();
        }
        if (teamService.findOne(sprintTeam.getTeam().getId()) == null) {
            throw new TeamNotFoundException();
        }

        SprintTeam responseSprintTeam = sprintTeamRepository.save(sprintTeam);
        // Since the fields of the referenced entities (iteration and team) are not filled correctly by the repository
        // we have to retrieve and assign them manually here.
        responseSprintTeam.setSprint(iterationService.findOne(responseSprintTeam.getSprint().getId()));
        responseSprintTeam.setTeam(teamService.findOne(responseSprintTeam.getTeam().getId()));
        return responseSprintTeam;
    }

    /**
     *  Get all the sprintTeams.
     *
     *  @return the list of entities
     */
    @Override
    public List<SprintTeam> findAll() {
        log.debug("Request to get all SprintTeams");
        return sprintTeamRepository.findAll();
    }

    /**
     *  Get one sprintTeam by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public SprintTeam findOne(String id) {
        log.debug("Request to get SprintTeam : {}", id);
        return sprintTeamRepository.findOne(id);
    }

    /**
     *  Delete the  sprintTeam by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete SprintTeam : {}", id);

        if (sprintTeamRepository.findOne(id) == null) {
            throw new SprintTeamNotFoundException();
        }

        sprintTeamRepository.delete(id);
    }
}
