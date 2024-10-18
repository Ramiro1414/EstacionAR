package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
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

    public List<RegistroConductor> findByPatente(String patente) {
        List<RegistroConductor> result = new ArrayList<>();
        repository.findByPatente(patente).forEach(e -> result.add(e));
        return result;
    }

    public List<RegistroConductor> getRegistrosConductorConFechaHastaHoyNoVerificadosDeUnaPatente(String patente) {
        return repository.getRegistrosConductorConFechaHastaHoyNoVerificadosDeUnaPatente(patente);
    }

    public List<RegistroConductor> getRegistrosConductorConFechaHastaHoyNoVerificados() {
        return repository.getRegistrosConductorConFechaHastaHoyNoVerificados();
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

    public boolean registroConductorExiste(String patente) {

        List<RegistroConductor> registrosConductores = this.findAllOrderByPatenteAsc();

        for (RegistroConductor r : registrosConductores) {
            if (r.getPatente().equals(patente)) {
                return true;
            }
        }
        
        return false;
    }

    public RegistroConductor encontrarRegistroConductorParaElGrupo(String patente, Timestamp horaMin, Timestamp horaMax) {     
    // Timestamp encontrar registros de esa patente que el horaMax este despues del inicio y el horaMin este antes del fin

    List<RegistroConductor> registrosConductor = (List<RegistroConductor>) repository.findAll();

    for (RegistroConductor r : registrosConductor) {
        if (r.getPatente().equals(patente)) {
            if (horaMax.after(r.getHoraInicio()) && horaMin.before(r.getHoraFin())) {
                return r;
            }
        }
    }

    return null;
    }
}