package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.RegistroConductor;

@Service
public class RegistroConductorService {
    
    @Autowired
    RegistroConductorRepository repository;

    public List<RegistroConductor> findAll(){
        List<RegistroConductor> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public List<RegistroConductor> findAllOrderByPatenteAsc() {
        
        List<RegistroConductor> result = new ArrayList<>();
        
        repository.findAll().forEach(result::add);
        
        result.sort(Comparator.comparing(RegistroConductor::getPatente));
    
        return result;
    }

    @Transactional
    public RegistroConductor save(RegistroConductor e){
        return repository.save(e);
    }

    public RegistroConductor findById(int id) { 
        return repository.findById(id).orElse(null);
    }
}