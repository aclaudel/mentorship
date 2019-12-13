package aclaudel.codurance.service;

import aclaudel.codurance.domain.Craftsperson;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Craftsperson}.
 */
public interface CraftspersonService {

    /**
     * Save a craftsperson.
     *
     * @param craftsperson the entity to save.
     * @return the persisted entity.
     */
    Craftsperson save(Craftsperson craftsperson);

    /**
     * Get all the craftspeople.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Craftsperson> findAll(Pageable pageable);


    /**
     * Get the "id" craftsperson.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Craftsperson> findOne(Long id);

    /**
     * Delete the "id" craftsperson.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
