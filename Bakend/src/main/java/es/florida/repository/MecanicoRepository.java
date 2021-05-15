package es.florida.repository;

import es.florida.domain.Mecanico;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mecanico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Long>, JpaSpecificationExecutor<Mecanico> {}
