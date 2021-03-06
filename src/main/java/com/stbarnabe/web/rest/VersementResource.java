package com.stbarnabe.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.stbarnabe.domain.Versement;
import com.stbarnabe.service.VersementService;
import com.stbarnabe.web.rest.errors.BadRequestAlertException;
import com.stbarnabe.web.rest.util.HeaderUtil;
import com.stbarnabe.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Versement.
 */
@RestController
@RequestMapping("/api")
public class VersementResource {

    private final Logger log = LoggerFactory.getLogger(VersementResource.class);

    private static final String ENTITY_NAME = "versement";

    private final VersementService versementService;

    public VersementResource(VersementService versementService) {
        this.versementService = versementService;
    }

    /**
     * POST  /versements : Create a new versement.
     *
     * @param versement the versement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new versement, or with status 400 (Bad Request) if the versement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/versements")
    @Timed
    public ResponseEntity<Versement> createVersement(@Valid @RequestBody Versement versement) throws URISyntaxException {
        log.debug("REST request to save Versement : {}", versement);
        if (versement.getId() != null) {
            throw new BadRequestAlertException("A new versement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Versement result = versementService.save(versement);
        return ResponseEntity.created(new URI("/api/versements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /versements : Updates an existing versement.
     *
     * @param versement the versement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated versement,
     * or with status 400 (Bad Request) if the versement is not valid,
     * or with status 500 (Internal Server Error) if the versement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/versements")
    @Timed
    public ResponseEntity<Versement> updateVersement(@Valid @RequestBody Versement versement) throws URISyntaxException {
        log.debug("REST request to update Versement : {}", versement);
        if (versement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Versement result = versementService.save(versement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, versement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /versements : get all the versements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of versements in body
     */
    @GetMapping("/versements")
    @Timed
    public ResponseEntity<List<Versement>> getAllVersements(Pageable pageable) {
        log.debug("REST request to get a page of Versements");
        Page<Versement> page = versementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/versements");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /versements/:id : get the "id" versement.
     *
     * @param id the id of the versement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the versement, or with status 404 (Not Found)
     */
    @GetMapping("/versements/{id}")
    @Timed
    public ResponseEntity<Versement> getVersement(@PathVariable Long id) {
        log.debug("REST request to get Versement : {}", id);
        Optional<Versement> versement = versementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(versement);
    }

    /**
     * DELETE  /versements/:id : delete the "id" versement.
     *
     * @param id the id of the versement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/versements/{id}")
    @Timed
    public ResponseEntity<Void> deleteVersement(@PathVariable Long id) {
        log.debug("REST request to delete Versement : {}", id);
        versementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
