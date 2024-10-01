package unpsjb.labprog.backend.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import unpsjb.labprog.backend.model.PatronPatente;

public interface PatronesPatentesRepository  extends CrudRepository<PatronPatente, Integer>, PagingAndSortingRepository<PatronPatente, Integer> {

    

}
