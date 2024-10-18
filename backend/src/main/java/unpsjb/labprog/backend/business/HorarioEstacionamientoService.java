package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.HoraInicioHoraFin;
import unpsjb.labprog.backend.model.HorarioEstacionamiento;

@Service
public class HorarioEstacionamientoService {
    
    @Autowired
    HorarioEstacionamientoRepository repository;

    public List<HorarioEstacionamiento> findAll() {
        List<HorarioEstacionamiento> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public HorarioEstacionamiento findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public HorarioEstacionamiento save(HorarioEstacionamiento unHorarioEstacionamiento) {
        return repository.save(unHorarioEstacionamiento);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<HorarioEstacionamiento> findByPage(int page, int size, Sort sort) {
        return repository.findAll(PageRequest.of(page, size, sort));
    }

    @SuppressWarnings("unchecked")
    public List<HoraInicioHoraFin> findHorariosDelDia(int id) {
        List<HoraInicioHoraFin> horarios = (List) repository.findById(id).get().getHorariosDelDia();
        
        // Ordenar la lista por el atributo "horaInicio"
        horarios.sort(Comparator.comparing(HoraInicioHoraFin::getHoraInicio));
        
        return horarios;
    }
}
