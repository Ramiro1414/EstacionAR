package unpsjb.labprog.backend.business;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import unpsjb.labprog.backend.model.HoraInicioHoraFin;
import unpsjb.labprog.backend.model.HorarioEstacionamiento;


@Repository
public interface  HorarioEstacionamientoRepository extends CrudRepository<HorarioEstacionamiento, Integer>, PagingAndSortingRepository<HorarioEstacionamiento, Integer> {
    
}
