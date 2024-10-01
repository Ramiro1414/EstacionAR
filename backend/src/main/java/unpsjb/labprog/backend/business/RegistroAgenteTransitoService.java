package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.RegistroAgenteTransito;

@Service
public class RegistroAgenteTransitoService {
    
    // Constante que define la maxima diferencia que se permite al comparar las ubicaciones de registros de agentes de transito
    public static int MAXIMA_DIFERENCIA_DISTANCIA = 5;

    @Autowired
    RegistroAgenteTransitoRepository repository;

    public List<RegistroAgenteTransito> findAll(){
        List<RegistroAgenteTransito> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    /** Retorna una lista de registros de agentes de transito ordenada por patentes en orden ascendente */
    public List<RegistroAgenteTransito> findAllOrderByPatenteAsc() {
        
        return repository.getRegistrosObleistaPorPatente();

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

    // public List<List<RegistroAgenteTransito>> agruparPorUbicacionRegistroAgentesTransito(List<RegistroAgenteTransito> registrosAgenteTransito) {
    
    // List<List<RegistroAgenteTransito>> result;
    // boolean termina = false;
    // RegistroAgenteTransito first;
    // List<RegistroAgenteTransito> group;
    // double distancia = 0;

    // while(!termina) {
    //     first = registrosAgenteTransito.get(0);
    //     group = null;
    //     for(RegistroAgenteTransito r : registrosAgenteTransito) {

    //         double[] coordenadaA = {r.getLatitud(), r.getLongitud()};
    //         double[] coordenadaA = {r.getLatitud(), r.getLongitud()};

    //         distancia = Haversine.distanciaEntrePuntos(coordenadaA, coordenadaB);

    //         if(distancia <= MAXIMA_DIFERENCIA_DISTANCIA) {
    //             group.add(r);
    //             registrosAgenteTransito.remove(r);
    //         }

    //     }

    //     result.add(group);

    //     if (registrosAgenteTransito.isEmpty()) { termina = true; }
    // }

    // return result;
    // }

    @Transactional
    public RegistroAgenteTransito save(RegistroAgenteTransito e){
        return repository.save(e);
    }

    public RegistroAgenteTransito findById(int id) { 
        return repository.findById(id).orElse(null);
    }
}