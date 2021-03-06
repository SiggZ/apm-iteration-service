package tum.sebis.apm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tum.sebis.apm.client.PersonServiceClient;
import tum.sebis.apm.domain.Iteration;
import tum.sebis.apm.domain.Person;
import tum.sebis.apm.domain.SprintTeam;
import tum.sebis.apm.domain.SprintTeamPerson;
import tum.sebis.apm.domain.Team;
import tum.sebis.apm.repository.SprintTeamRepository;
import tum.sebis.apm.service.IterationService;
import tum.sebis.apm.service.SprintTeamService;
import tum.sebis.apm.service.TeamService;
import tum.sebis.apm.web.rest.errors.IdMustNotBeNullException;
import tum.sebis.apm.web.rest.errors.SprintNotFoundException;
import tum.sebis.apm.web.rest.errors.SprintTeamNotFoundException;
import tum.sebis.apm.web.rest.errors.TeamNotFoundException;
import tum.sebis.apm.web.rest.errors.PersonNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final PersonServiceClient personServiceClient;

    public SprintTeamServiceImpl(SprintTeamRepository sprintTeamRepository, IterationService iterationService, TeamService teamService, PersonServiceClient personServiceClient) {
        this.sprintTeamRepository = sprintTeamRepository;
        this.iterationService = iterationService;
        this.teamService = teamService;
        this.personServiceClient = personServiceClient;
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

        // Since the fields of the referenced entities (sprint and team) are not filled correctly by the repository
        // we have to retrieve and assign them manually here. (Before(!) calculating the available days below)
        sprintTeam.setSprint(sprint);
        sprintTeam.setTeam(team);

        if (sprintTeam.getSprintTeamPersons() == null) {
            sprintTeam.setSprintTeamPersons(Collections.emptyList());
        } else {
            sprintTeam.setSprintTeamPersons(removeDuplicates(sprintTeam.getSprintTeamPersons()));
            checkSprintTeamMembers(sprintTeam.getSprintTeamPersons());
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
        SprintTeam sprintTeam = sprintTeamRepository.findOne(id);
        if (sprintTeam == null) {
            throw new SprintTeamNotFoundException();
        }
        return sprintTeam;
    }

    /**
     *  Get all the sprintTeams of a given sprint.
     *
     *  @param sprint
     *  @return the list of entities
     */
    @Override
    public List<SprintTeam> findBySprint(Iteration sprint) {
        return sprintTeamRepository.findBySprintOrderByTeam(sprint);
    }

    /**
     *  Get all the sprintTeams of a given sprint.
     *
     *  @param sprint
     *  @return the list of entities
     */
    @Override
    public SprintTeam findBySprintAndTeam(Iteration sprint, Team team) {
        return sprintTeamRepository.findBySprintAndTeam(sprint, team);
    }





    /**
     *  Delete the sprintTeam by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete SprintTeam : {}", id);
        if (findOne(id) != null) {
            sprintTeamRepository.delete(id);
        }
    }

    /**
     *  Delete all sprintTeams of a given sprint.
     *
     *  @param sprint the sprint
     */
    @Override
    public void deleteForSprint(Iteration sprint) {
        List<SprintTeam> sprintTeamsToDelete = findBySprint(sprint);
        if (sprintTeamsToDelete != null) {
            for (SprintTeam sprintTeam : sprintTeamsToDelete) {
                delete(sprintTeam.getId());
            }
        }
    }

    /**
     *  Calculate the capacity for the SprintTeam with the given id
     *
     * @param sprintTeamId the id of the sprint team for which to calculate the capacity
     * @return the calculated capacity
     */
    @Override
    public double calculateCapacity(String sprintTeamId) {
        log.debug("Request to calculate capacity for SprintTeam : {}", sprintTeamId);
        double capacity = 0;
        for (SprintTeamPerson sprintTeamPerson : findOne(sprintTeamId).getSprintTeamPersons()) {
            Person person = personServiceClient.getPersonById(sprintTeamPerson.getPersonId());
            capacity +=
                sprintTeamPerson.getAvailableDays().size() * person.getProjectAvailability() * person.getSprintAvailability();
        }
        return round(capacity, 2);
    }

    /**
     *  Calculate the velocity for the SprintTeam with the given id
     *
     * @param sprintTeamId the id of the sprint team for which to calculate the velocity
     * @return the calculated velocity
     */
    @Override
    public double calculateVelocity(String sprintTeamId) {
        double velocity = calculateCapacity(sprintTeamId) * findOne(sprintTeamId).getVelocityFactor();
        return round(velocity, 2);
    }

    /**
     *  Update the available days of the members of all sprint teams in the given sprint
     *
     * @param sprint the sprint for which to update the available days
     */
    @Override
    public void updateAvailableDays(Iteration sprint) {
       List<SprintTeam> sprintTeams = sprintTeamRepository.findBySprint(sprint);
       if (sprintTeams != null) {
           for (SprintTeam sprintTeam : sprintTeams) {
               sprintTeam = calculateAvailableDays(sprintTeam);
               save(sprintTeam);
           }
       }
    }

    /**
     *  Removes duplicate entries from a list of SprintTeamPersons.
     *
     * @param members the list of SprintTeamPersons
     * @return the list of SprintTeamPersons without duplicates
     */
    private List<SprintTeamPerson> removeDuplicates(List<SprintTeamPerson> members) {
        Map<String,SprintTeamPerson> map = new HashMap<>();
        for (SprintTeamPerson member : members) {
            map.put(member.getPersonId(), member);
        }
        members.clear();
        members.addAll(map.values());
        return members;
    }

    /**
     *  Check, if the members specified in a sprint team exist by querying
     *  the person service client with the person ids.
     *  If one of the person ids does not belong to an existing person, a {@link PersonNotFoundException}
     *  is thrown.
     * @param members a list of SprintTeamPersons
     */
    private void checkSprintTeamMembers(List<SprintTeamPerson> members) {
        for (SprintTeamPerson member : members) {
            if (member.getPersonId() == null || !personServiceClient.isPersonExisting(member.getPersonId())) {
                //TODO: maybe just ignore member entries with non-existing person ids instead of throwing an exception?
                throw new PersonNotFoundException(member.getPersonId());
            }
        }
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

        for (SprintTeamPerson sprintTeamPerson : sprintTeam.getSprintTeamPersons()) {
            sprintTeamPerson.setAvailableDays(getListOfWeekdays(startDate, endDate));
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

    /**
     *  Rounds a value to a given number of decimals
     *
     * @param value the value that should be rounded
     * @param places the number of decimals
     * @return the rounded value
     */
    private static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
