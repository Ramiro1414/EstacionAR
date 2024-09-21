package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.RegistroAgenteTransito;
import unpsjb.labprog.backend.model.RegistroConductor;
import unpsjb.labprog.backend.model.RegistroInfraccion;

@Service
public class RegistroInfraccionService {
    
    @Autowired
    RegistroInfraccionRepository repository;
    @Autowired
    RegistroAgenteTransitoRepository registroAgenteTransitoRepository;
    @Autowired
    RegistroConductorRepository registroConductorRepository;
    @Autowired
    RegistroAgenteTransitoService registroAgenteTransitoService;
    @Autowired
    RegistroConductorService registroConductorService;

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

    /** Proceso que realiza el cruzamiento de registros de agentes de tránsito y registros de 
    conductores para determinar si estos últimos cometieron alguna infraccion o no.*/
    @Transactional
    public List<RegistroInfraccion> cruzamiento() {

        List<RegistroInfraccion> result = new ArrayList<>();
        
        List<RegistroConductor> registrosConductores = registroConductorService.findAllOrderByPatenteAsc();

        // Iteramos sobre los registros de conductores
        for (RegistroConductor rc : registrosConductores) {

            if (!registroAgenteTransitoService.contienePatente(rc.getPatente())) { // si la patente del conductor no esta en los registros de obleistas, continuo con la siguiente patente ya que no fue registrada por los obleistas y no comete infraccion
                
            } else { // si la patente del conductor se encuentra en los registros de obleistas, se debe verificar si comete o no infraccion

                // Iteramos sobre los registros de agentes de transito de esa patente
                List<RegistroAgenteTransito> registroAgenteTransitosPatente = registroAgenteTransitoService.registrosDeUnaPatente(rc.getPatente());
                
                for (RegistroAgenteTransito rat : registroAgenteTransitosPatente) {

                    if (rat.getHoraRegistro().after(rc.getHoraInicio()) && rat.getHoraRegistro().before(rc.getHoraFin())) { // no comete infraccion

                    } else { // comete infraccion
                        RegistroInfraccion nuevoRegistroInfraccion = new RegistroInfraccion();
                        nuevoRegistroInfraccion.setRegistroAgenteTransito(rat);
                        nuevoRegistroInfraccion.setRegistroConductor(rc);
                        result.add(this.save(nuevoRegistroInfraccion)); // agrego la infraccion a la lista y guardo el registro de infraccion en la base de datos
                    }
                }
            }
        }

        return result;

    }
}