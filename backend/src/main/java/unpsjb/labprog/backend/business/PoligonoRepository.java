package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.LineaPoligono;
import unpsjb.labprog.backend.model.Poligono;

@Repository
public interface PoligonoRepository extends CrudRepository<Poligono, Integer>, PagingAndSortingRepository<Poligono, Integer> {

}
