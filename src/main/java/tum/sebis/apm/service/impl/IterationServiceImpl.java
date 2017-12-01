package tum.sebis.apm.service.impl;

import tum.sebis.apm.service.IterationService;
import tum.sebis.apm.domain.Iteration;
import tum.sebis.apm.repository.IterationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Implementation for managing Iteration.
 */
@Service
public class IterationServiceImpl implements IterationService{

    private final Logger log = LoggerFactory.getLogger(IterationServiceImpl.class);

    private final IterationRepository iterationRepository;

    public IterationServiceImpl(IterationRepository iterationRepository) {
        this.iterationRepository = iterationRepository;
    }

    /**
     * Save a iteration.
     *
     * @param iteration the entity to save
     * @return the persisted entity
     */
    @Override
    public Iteration save(Iteration iteration) {
        log.debug("Request to save Iteration : {}", iteration);
        return iterationRepository.save(iteration);
    }

    /**
     *  Get all the iterations.
     *
     *  @return the list of entities
     */
    @Override
    public List<Iteration> findAll() {
        log.debug("Request to get all Iterations");
        return iterationRepository.findAll();
    }

    /**
     *  Get one iteration by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    public Iteration findOne(String id) {
        log.debug("Request to get Iteration : {}", id);
        return iterationRepository.findOne(id);
    }

    /**
     *  Delete the  iteration by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Iteration : {}", id);
        iterationRepository.delete(id);
    }
}
