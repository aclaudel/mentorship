package aclaudel.codurance.service.impl;

import aclaudel.codurance.service.CraftspersonService;
import aclaudel.codurance.domain.Craftsperson;
import aclaudel.codurance.repository.CraftspersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Craftsperson}.
 */
@Service
@Transactional
public class CraftspersonServiceImpl implements CraftspersonService {

    private final Logger log = LoggerFactory.getLogger(CraftspersonServiceImpl.class);

    private final CraftspersonRepository craftspersonRepository;

    public CraftspersonServiceImpl(CraftspersonRepository craftspersonRepository) {
        this.craftspersonRepository = craftspersonRepository;
    }

    /**
     * Save a craftsperson.
     *
     * @param craftsperson the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Craftsperson save(Craftsperson craftsperson) {
        log.debug("Request to save Craftsperson : {}", craftsperson);
        return craftspersonRepository.save(craftsperson);
    }

    /**
     * Get all the craftspeople.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Craftsperson> findAll(Pageable pageable) {
        log.debug("Request to get all Craftspeople");
        return craftspersonRepository.findAll(pageable);
    }


    /**
     * Get one craftsperson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Craftsperson> findOne(Long id) {
        log.debug("Request to get Craftsperson : {}", id);
        return craftspersonRepository.findById(id);
    }

    /**
     * Delete the craftsperson by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Craftsperson : {}", id);
        craftspersonRepository.deleteById(id);
    }
}
