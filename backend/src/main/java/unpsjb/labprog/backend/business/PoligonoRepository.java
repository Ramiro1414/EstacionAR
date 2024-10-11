package unpsjb.labprog.backend.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.Poligono;

@Repository
public interface PoligonoRepository extends CrudRepository<Poligono, Integer>, PagingAndSortingRepository<Poligono, Integer> {

}
