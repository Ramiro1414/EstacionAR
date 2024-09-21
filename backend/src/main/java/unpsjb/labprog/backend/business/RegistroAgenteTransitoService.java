package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.Comparator;
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

    /** Retorna una lista de registros de agentes de transito ordenada por patentes en orden ascendente */
    public List<RegistroAgenteTransito> findAllOrderByPatenteAsc() {
        
        List<RegistroAgenteTransito> result = new ArrayList<>();
        
        repository.findAll().forEach(result::add);
        
        result.sort(Comparator.comparing(RegistroAgenteTransito::getPatente));
    
        return result;
    }

    /** Retorna un booleano en funcion de si una patente fue registrada o no por los agentes de transito */
    public boolean contienePatente(String patente) {

        List<RegistroAgenteTransito> registros = this.findAllOrderByPatenteAsc();

        for (RegistroAgenteTransito r : registros) {
            if (patente.equals(r.getPatente())) {
                return true;
            }
        }
        
        return false;
    }

    /** Retorna una lista de los registros de agentes de transito sobre una patente */
    public List<RegistroAgenteTransito> registrosDeUnaPatente(String patente) {

        List<RegistroAgenteTransito> registros = this.findAllOrderByPatenteAsc();

        List<RegistroAgenteTransito> result = new ArrayList<>();

        for (RegistroAgenteTransito rat : registros) {
            if (patente.equals(rat.getPatente())) {
                result.add(rat);
            }
        }

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