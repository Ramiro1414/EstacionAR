package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.RegistroAgenteTransito;

@Service
public class RegistroAgenteTransitoService {
    
    @Autowired
    RegistroAgenteTransitoRepository repository;

    public List<RegistroAgenteTransito> findAll(){
        List<RegistroAgenteTransito> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    @Transactional
    public RegistroAgenteTransito save(RegistroAgenteTransito e){
        return repository.save(e);
    }

    public RegistroAgenteTransito findById(int id) { 
        return repository.findById(id).orElse(null);
    }
}