package unpsjb.labprog.backend.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Punto;

@Repository
public interface PuntoRepository extends CrudRepository<Punto, Integer>{
    
}
