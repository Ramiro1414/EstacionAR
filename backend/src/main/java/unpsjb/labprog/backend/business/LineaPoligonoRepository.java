package unpsjb.labprog.backend.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.LineaPoligono;

@Repository
public interface LineaPoligonoRepository extends CrudRepository<LineaPoligono, Integer> {

}
