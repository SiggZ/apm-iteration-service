package tum.sebis.apm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tum.sebis.apm.domain.Iteration;
import tum.sebis.apm.domain.Person;
import tum.sebis.apm.domain.SprintTeam;
import tum.sebis.apm.domain.Team;
import tum.sebis.apm.repository.SprintTeamRepository;
import tum.sebis.apm.service.IterationService;
import tum.sebis.apm.service.SprintTeamService;
import tum.sebis.apm.service.TeamService;
import tum.sebis.apm.web.rest.errors.IdMustNotBeNullException;
import tum.sebis.apm.web.rest.errors.SprintNotFoundException;
import tum.sebis.apm.web.rest.errors.SprintTeamNotFoundException;
import tum.sebis.apm.web.rest.errors.TeamNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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

        // TODO: adjust this validation?
//        if (sprintTeamRepository.findBySprintAndTeam(sprintTeam.getSprint(), sprintTeam.getTeam()).size() > 0) {
//            throw new TeamAlreadyInSprintException();
//        }
        String sprintId = sprintTeam.getSprint().getId();
        if (sprintId == null || sprintId.isEmpty()) {
            throw new IdMustNotBeNullException("Sprint");
        }
        String teamId = sprintTeam.getTeam().getId();
        if (teamId == null || teamId.isEmpty()) {
            throw new IdMustNotBeNullException("Team");
        }
        Iteration sprint = iterationService.findOne(sprintId);
        if (sprint == null) {
            throw new SprintNotFoundException();
        }
        Team team = teamService.findOne(teamId);
        if (team == null) {
            throw new TeamNotFoundException();
        }
        // TODO: validation for persons? error if persons do not exist?

        // Since the fields of the referenced entities (sprint and team) are not filled correctly by the repository
        // we have to retrieve and assign them manually here.
        sprintTeam.setSprint(sprint);
        sprintTeam.setTeam(team);

        if (sprintTeam.getPersons() == null) {
            sprintTeam.setPersons(new ArrayList<>());
        } else {
            sprintTeam = calculateAvailableDays(sprintTeam);
        }

        SprintTeam responseSprintTeam = sprintTeamRepository.save(sprintTeam);
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

    /**
     *  Calculate the days that the persons of the sprint team are available in the corresponding sprint
     *
     *  @param sprintTeam
     *  @return the sprintTeam entity with the calculated availabilities
     */
    private SprintTeam calculateAvailableDays(SprintTeam sprintTeam) {
        LocalDate startDate = sprintTeam.getSprint().getStart();
        LocalDate endDate = sprintTeam.getSprint().getEnd();

        for (Person person : sprintTeam.getPersons()) {
            person.setAvailableDays(getListOfWeekdays(startDate, endDate));
        }
        return sprintTeam;
    }

    /**
     *  Generate a list of LocalDate between given startDate and endDate without weekends
     *
     * @param startDate
     * @param endDate
     * @return a list of LocalDate without weekends
     */
    private List<LocalDate> getListOfWeekdays(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> listOfDays = new ArrayList<>();
        Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            if (!weekend.contains(d.getDayOfWeek())) {
                listOfDays.add(d);
            }
        }
        return listOfDays;
    }
}
