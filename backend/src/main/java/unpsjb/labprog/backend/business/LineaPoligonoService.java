package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.LineaPoligono;

@Service
public class LineaPoligonoService {

    @Autowired
    LineaPoligonoRepository repository;
    
    public List<LineaPoligono> findAll() {
        List<LineaPoligono> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public LineaPoligono findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public LineaPoligono save(LineaPoligono unaLineaPoligono) {
        return repository.save(unaLineaPoligono);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }
}
