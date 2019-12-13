package aclaudel.codurance.repository;
import aclaudel.codurance.domain.Craftsperson;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Craftsperson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CraftspersonRepository extends JpaRepository<Craftsperson, Long> {

}
