package tum.sebis.apm.repository;

import tum.sebis.apm.domain.SprintTeam;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the SprintTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SprintTeamRepository extends MongoRepository<SprintTeam, String> {

}
