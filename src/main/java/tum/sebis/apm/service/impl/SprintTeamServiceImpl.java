package tum.sebis.apm.service.impl;

import tum.sebis.apm.service.SprintTeamService;
import tum.sebis.apm.domain.SprintTeam;
import tum.sebis.apm.repository.SprintTeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing SprintTeam.
 */
@Service
public class SprintTeamServiceImpl implements SprintTeamService{

    private final Logger log = LoggerFactory.getLogger(SprintTeamServiceImpl.class);

    private final SprintTeamRepository sprintTeamRepository;

    public SprintTeamServiceImpl(SprintTeamRepository sprintTeamRepository) {
        this.sprintTeamRepository = sprintTeamRepository;
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
        return sprintTeamRepository.save(sprintTeam);
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
        sprintTeamRepository.delete(id);
    }
}
