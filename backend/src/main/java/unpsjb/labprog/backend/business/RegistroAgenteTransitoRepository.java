package unpsjb.labprog.backend.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.RegistroAgenteTransito;

@Repository
public interface RegistroAgenteTransitoRepository extends CrudRepository<RegistroAgenteTransito, Integer> {
    
}