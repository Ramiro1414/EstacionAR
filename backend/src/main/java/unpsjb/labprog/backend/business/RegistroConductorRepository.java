package unpsjb.labprog.backend.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.RegistroConductor;

@Repository
public interface RegistroConductorRepository extends CrudRepository<RegistroConductor, Integer>{
    
}
