package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.RegistroInfraccion;

@Service
public class RegistroInfraccionService {
    
    @Autowired
    RegistroInfraccionRepository repository;

        public List<RegistroInfraccion> findAll(){
        List<RegistroInfraccion> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    @Transactional
    public RegistroInfraccion save(RegistroInfraccion e){
        return repository.save(e);
    }

    public RegistroInfraccion findById(int id) { 
        return repository.findById(id).orElse(null);
    }
}