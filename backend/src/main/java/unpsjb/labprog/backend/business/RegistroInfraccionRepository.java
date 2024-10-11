package unpsjb.labprog.backend.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.RegistroInfraccion;

@Repository
public interface RegistroInfraccionRepository extends CrudRepository<RegistroInfraccion, Integer>, PagingAndSortingRepository<RegistroInfraccion, Integer>{
    
}