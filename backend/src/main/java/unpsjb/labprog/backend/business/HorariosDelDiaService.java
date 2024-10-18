package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.HoraInicioHoraFin;

@Service
public class HorariosDelDiaService {
    
    @Autowired
    HorariosDelDiaRepository repository;

    public List<HoraInicioHoraFin> findAll() {
        List<HoraInicioHoraFin> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public HoraInicioHoraFin findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public HoraInicioHoraFin save(HoraInicioHoraFin aHoraInicioHoraFin) {
        return repository.save(aHoraInicioHoraFin);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }
    
}
