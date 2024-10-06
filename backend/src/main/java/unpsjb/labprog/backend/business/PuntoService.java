package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.Punto;

@Service
public class PuntoService {

    @Autowired
    PuntoRepository repository;

    public List<Punto> findAll() {
        List<Punto> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public Punto findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Punto save(Punto unPunto) {
        return repository.save(unPunto);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }
    
}
