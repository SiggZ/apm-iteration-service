package tum.sebis.apm.repository;

import tum.sebis.apm.domain.Team;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends MongoRepository<Team, String> {

}
