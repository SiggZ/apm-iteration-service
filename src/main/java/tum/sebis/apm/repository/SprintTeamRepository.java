package tum.sebis.apm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tum.sebis.apm.domain.Iteration;
import tum.sebis.apm.domain.SprintTeam;
import tum.sebis.apm.domain.Team;

import java.util.List;

/**
 * Spring Data MongoDB repository for the SprintTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SprintTeamRepository extends MongoRepository<SprintTeam, String> {

    List<SprintTeam> findBySprintAndTeam(Iteration sprint, Team team);
    List<SprintTeam> findBySprint(Iteration sprint);
    List<SprintTeam> findByTeam (Team team);
}
