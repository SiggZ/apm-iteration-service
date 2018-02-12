package tum.sebis.apm.repository;

import tum.sebis.apm.domain.Iteration;
import org.springframework.stereotype.Repository;
import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Iteration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IterationRepository extends MongoRepository<Iteration, String> {
        List<Iteration> findAllByOrderByStartDesc();
}
