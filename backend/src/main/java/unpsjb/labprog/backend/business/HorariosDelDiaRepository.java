package unpsjb.labprog.backend.business;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.HoraInicioHoraFin;

@Repository
public interface HorariosDelDiaRepository extends CrudRepository<HoraInicioHoraFin, Integer> {

}
