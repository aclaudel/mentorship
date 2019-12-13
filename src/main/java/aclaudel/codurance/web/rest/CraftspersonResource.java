package aclaudel.codurance.web.rest;

import aclaudel.codurance.domain.Craftsperson;
import aclaudel.codurance.service.CraftspersonService;
import aclaudel.codurance.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link aclaudel.codurance.domain.Craftsperson}.
 */
@RestController
@RequestMapping("/api")
public class CraftspersonResource {

    private final Logger log = LoggerFactory.getLogger(CraftspersonResource.class);

    private static final String ENTITY_NAME = "craftsperson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CraftspersonService craftspersonService;

    public CraftspersonResource(CraftspersonService craftspersonService) {
        this.craftspersonService = craftspersonService;
    }

    /**
     * {@code POST  /craftspeople} : Create a new craftsperson.
     *
     * @param craftsperson the craftsperson to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new craftsperson, or with status {@code 400 (Bad Request)} if the craftsperson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/craftspeople")
    public ResponseEntity<Craftsperson> createCraftsperson(@RequestBody Craftsperson craftsperson) throws URISyntaxException {
        log.debug("REST request to save Craftsperson : {}", craftsperson);
        if (craftsperson.getId() != null) {
            throw new BadRequestAlertException("A new craftsperson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Craftsperson result = craftspersonService.save(craftsperson);
        return ResponseEntity.created(new URI("/api/craftspeople/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /craftspeople} : Updates an existing craftsperson.
     *
     * @param craftsperson the craftsperson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated craftsperson,
     * or with status {@code 400 (Bad Request)} if the craftsperson is not valid,
     * or with status {@code 500 (Internal Server Error)} if the craftsperson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/craftspeople")
    public ResponseEntity<Craftsperson> updateCraftsperson(@RequestBody Craftsperson craftsperson) throws URISyntaxException {
        log.debug("REST request to update Craftsperson : {}", craftsperson);
        if (craftsperson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Craftsperson result = craftspersonService.save(craftsperson);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, craftsperson.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /craftspeople} : get all the craftspeople.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of craftspeople in body.
     */
    @GetMapping("/craftspeople")
    public ResponseEntity<List<Craftsperson>> getAllCraftspeople(Pageable pageable) {
        log.debug("REST request to get a page of Craftspeople");
        Page<Craftsperson> page = craftspersonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /craftspeople/:id} : get the "id" craftsperson.
     *
     * @param id the id of the craftsperson to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the craftsperson, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/craftspeople/{id}")
    public ResponseEntity<Craftsperson> getCraftsperson(@PathVariable Long id) {
        log.debug("REST request to get Craftsperson : {}", id);
        Optional<Craftsperson> craftsperson = craftspersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(craftsperson);
    }

    /**
     * {@code DELETE  /craftspeople/:id} : delete the "id" craftsperson.
     *
     * @param id the id of the craftsperson to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/craftspeople/{id}")
    public ResponseEntity<Void> deleteCraftsperson(@PathVariable Long id) {
        log.debug("REST request to delete Craftsperson : {}", id);
        craftspersonService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
