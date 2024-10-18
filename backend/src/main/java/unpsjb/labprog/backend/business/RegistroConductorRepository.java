package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.RegistroConductor;

@Repository
public interface RegistroConductorRepository extends CrudRepository<RegistroConductor, Integer>{
    
    @Query("SELECT r FROM RegistroConductor r WHERE r.patente = ?1")
    List<RegistroConductor> findByPatente(String patente);

    @Query("SELECT r FROM RegistroConductor r WHERE r.verificado = false")
    List<RegistroConductor> getRegistrosConductorConFechaHastaHoyNoVerificados();

    @Query("SELECT r FROM RegistroConductor r WHERE r.verificado = false AND r.patente = ?1")
    List<RegistroConductor> getRegistrosConductorConFechaHastaHoyNoVerificadosDeUnaPatente(String patente);
}
