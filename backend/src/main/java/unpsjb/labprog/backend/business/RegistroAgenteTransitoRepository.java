package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.RegistroAgenteTransito;

@Repository
public interface RegistroAgenteTransitoRepository extends CrudRepository<RegistroAgenteTransito, Integer> {
    
    // @Query(" SELECT r FROM RegistroObleista r WHERE r.patente = :patente AND FUNCTION('DATE', r.horaRegistro) = CURRENT_DATE ORDER BY r.horaRegistro")
    // List<RegistroAgenteTransito> getRegistrosObleistaPorPatente(@Param("patente") String patente);

    @Query("SELECT r FROM RegistroAgenteTransito r ORDER BY r.patente asc, r.horaRegistro asc")
    List<RegistroAgenteTransito> getRegistrosObleistaPorPatente();

}