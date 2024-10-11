package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.RegistroAgenteTransito;
import unpsjb.labprog.backend.model.RegistroConductor;
import unpsjb.labprog.backend.model.RegistroInfraccion;

@Service
public class RegistroInfraccionService {

    private static final Logger logger = LoggerFactory.getLogger(RegistroInfraccionService.class);

    private static final double DISTANCIA_ENTRE_UBICACIONES_MAXIMA = 5.0;

    @Autowired
    RegistroInfraccionRepository repository;
    @Autowired
    RegistroAgenteTransitoService registroAgenteTransitoService;
    @Autowired
    RegistroConductorService registroConductorService;

    public List<RegistroInfraccion> findAll() {
        List<RegistroInfraccion> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    @Transactional
    public RegistroInfraccion save(RegistroInfraccion e) {
        return repository.save(e);
    }

    public RegistroInfraccion findById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Page<RegistroInfraccion> findByPage(int page, int size, Sort sort) {
        return repository.findAll(PageRequest.of(page, size, sort));
    }

    /**
     * Proceso que realiza el cruzamiento de registros de agentes de tránsito y
     * registros de conductores para determinar si estos últimos cometieron
     * alguna infraccion o no.
     */
    /*
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
     */
 /*
     * conseguir todos los reg de agentes de transito de un dia ordenados por patente
     * 
     * agruparlos por la patente de cada uno (es decir en la primer iteracion se recorren los reg AT de la patente1 luego patente2 etc)
     * 
     * conseguir los registros de conductores de la patente actual de la iteracion
     * 
     * ver si el registro de obleista cae dentro de alguno de los registros de conductores
     * si esta afuera, es infraccion de esa patente en ese dia (obviar los demas registros de agentes de transito para esa patente y continuar con la siguiente patente)
     * 
     * (no tomar en cuenta ubicacion - hacer luego)
     * 
     */
    @Transactional
    public List<RegistroInfraccion> cruzamientoNuevo() {

        List<RegistroInfraccion> result = new ArrayList<>();

        List<RegistroAgenteTransito> registrosAgenteTransitoMezclados = registroAgenteTransitoService.findAllOrderByPatenteAsc();
        /* registrosAgenteTransitoMezclados es una lista de los RO ordenados por patente, pero son todas las patentes en todas las ubicaciones */
        for (List<RegistroAgenteTransito> grupoRegistrosAgenteTransitoDeUnaPatente : agruparPorPatente(registrosAgenteTransitoMezclados)) {
            /* agruparPorPatente devuelve una lista de listas de RO. Es una lista de los registros de cada patente, y los registros de cada patente es otra lista*/
            String patenteActual = grupoRegistrosAgenteTransitoDeUnaPatente.get(0).getPatente();
            /* tomo el primero, puede ser cualquiera porque pertenecen a una patente en comun*/
            for (List<RegistroAgenteTransito> grupoRegistrosAgenteTransitoPorUbicacion : agruparPorUbicacion(grupoRegistrosAgenteTransitoDeUnaPatente)) {
                /* agruparPorUbicacion devuelve una lista de los registros de cada ubicacion que estaciono esa patent, y los registros de cada ubicacion es otra lista */

                for (RegistroAgenteTransito r : grupoRegistrosAgenteTransitoPorUbicacion) {
                    System.out.println("id " + r.getId() + " patente " + r.getPatente());
                }
                System.out.println("--");

                List<RegistroConductor> registrosConductores = registroConductorService.findByPatente(patenteActual);
                /* Conseguir los RC de esa patente */
                for (RegistroAgenteTransito r : grupoRegistrosAgenteTransitoPorUbicacion) {
                    boolean hayInfraccion = true;
                    /* Para cada RO se comprobará si comete infraccion en el RC actual */
                    for (RegistroConductor rc : registrosConductores) {
                        /* Probar con cada RC */
                        if (r.getHoraRegistro().after(rc.getHoraInicio()) && r.getHoraRegistro().before(rc.getHoraFin())) {
                            hayInfraccion = false;
                            break;
                        }
                    }
                    if (hayInfraccion) {
                        RegistroInfraccion nuevaInfraccion = new RegistroInfraccion();
                        nuevaInfraccion.setRegistroAgenteTransito(r);
                        this.save(nuevaInfraccion);
                        result.add(nuevaInfraccion);
                        break;
                    }
                }
            }
        }

        return result;
    }

    /* retorna una lista de listas de registros de agente de transito. Es una lista de los registros de cada patente, y los registros de cada patente es otra lista */
    public List<List<RegistroAgenteTransito>> agruparPorPatente(List<RegistroAgenteTransito> registrosAgenteTransitoMezclados) {

        List<List<RegistroAgenteTransito>> result = new ArrayList<>();

        if (registrosAgenteTransitoMezclados.isEmpty()) { // devuelve una lista vacia si no hay registros de agente de transito
            return result;
        }

        String currentPatente = null; // variable utilizada para reconocer cuando aparece un registro con una nueva patente
        List<RegistroAgenteTransito> registrosAgenteTransito = new ArrayList<>();

        for (RegistroAgenteTransito rat : registrosAgenteTransitoMezclados) {
            String patenteRegistro = rat.getPatente(); // obtenemos la patente

            if (patenteRegistro == null || !patenteRegistro.equals(currentPatente)) { // verifica si la patente es diferente o null

                if (currentPatente != null) { // salvamos el primer caso, para no agregar una lista vacia
                    result.add(registrosAgenteTransito);
                }

                currentPatente = patenteRegistro; // asigno la patente de los registros
                registrosAgenteTransito = new ArrayList<>();
            }

            if (currentPatente != null) {
                registrosAgenteTransito.add(rat); // agregamos el registro a la lista de la patente actual
            }
        }

        if (!registrosAgenteTransito.isEmpty()) { // agregar la última lista de registros
            result.add(registrosAgenteTransito);
        }

        return result;
    }

    /* agruparPorUbicacion devuelve una lista de los registros de cada ubicacion que estaciono esa patent, y los registros de cada ubicacion es otra lista */
 /* 
     * grupoDeUBActual = grupoDeUB1;
     * 
     * while (!registrosAgenteTransitoDeUnaPatente.isEmpty()){
     *  anterior = registrosAgenteTransitoDeUnaPatente.get(0);
     *  for (actual : registrosAgenteTransitoDeUnaPatente){
     *      if(circle(anterior, actual)){
     *          grupoDeUBActual.add(actual);
     *          registrosAgenteTransitoDeUnaPatente.remove(actual); 
     *      } 
     *      anterior = actual;
     *  }
     *  result.add(grupoDeUBActual)
     *  grupoDeUBActual = new ArrayList();
     * }
     */
    public List<List<RegistroAgenteTransito>> agruparPorUbicacion(List<RegistroAgenteTransito> registrosAgenteTransitoDeUnaPatente) {
        List<List<RegistroAgenteTransito>> result = new ArrayList<>();

        if (registrosAgenteTransitoDeUnaPatente.isEmpty()) {
            return result;
        }

        List<Boolean> agrupado = new ArrayList<>(Collections.nCopies(registrosAgenteTransitoDeUnaPatente.size(), false));

        for (int i = 0; i < registrosAgenteTransitoDeUnaPatente.size(); i++) {
            if (agrupado.get(i)) {
                continue;
            }

            RegistroAgenteTransito referencia = registrosAgenteTransitoDeUnaPatente.get(i);
            List<RegistroAgenteTransito> grupoDeRegistrosUbicacionActual = new ArrayList<>();
            grupoDeRegistrosUbicacionActual.add(referencia);
            agrupado.set(i, true); // Marcamos el registro actual como agrupado

            // Recorremos toda la lista para agrupar los registros con la misma ubicación exacta
            for (int j = 0; j < registrosAgenteTransitoDeUnaPatente.size(); j++) {
                if (agrupado.get(j)) {
                    continue;
                }

                RegistroAgenteTransito actual = registrosAgenteTransitoDeUnaPatente.get(j);

                // Comparamos las ubicaciones exactamente
                if (Double.compare(referencia.getLatitud(), actual.getLatitud()) == 0
                        && Double.compare(referencia.getLongitud(), actual.getLongitud()) == 0) {
                    grupoDeRegistrosUbicacionActual.add(actual);
                    agrupado.set(j, true); // Marcamos como agrupado
                }
            }

            // Añadimos el grupo formado a la lista de resultados
            result.add(grupoDeRegistrosUbicacionActual);
        }

        return result;
    }

    @Transactional
    public List<RegistroInfraccion> cruzamiento() {

        List<RegistroInfraccion> result = new ArrayList<>();

        List<RegistroAgenteTransito> registrosAgenteTransito = registroAgenteTransitoService.findAllOrderByPatenteAsc();

        String currentPatente = null;

        boolean multaRealizada = false;

        boolean cometeInfraccion = false;

        for (RegistroAgenteTransito r : registrosAgenteTransito) {

            if (!r.getPatente().equals(currentPatente)) {
                currentPatente = r.getPatente();
                multaRealizada = false;
                cometeInfraccion = false;
            }

            List<RegistroConductor> registrosConductores = registroConductorService.findByPatente(currentPatente);
            logger.info("Cantidad de registros de conductores encontrados: " + registrosConductores.size());

            if (!multaRealizada) { // Si no se le realizó una multa a esa patente

                if (!registroConductorService.registroConductorExiste(r.getPatente())) {

                    RegistroInfraccion nuevaInfraccion = new RegistroInfraccion();
                    nuevaInfraccion.setRegistroAgenteTransito(r);
                    this.save(nuevaInfraccion);
                    result.add(nuevaInfraccion);
                    multaRealizada = true;

                } else { // Evaluo si cometió infraccion iterando sobre los registros de conductores

                    cometeInfraccion = true;

                    for (RegistroConductor rc : registrosConductores) {

                        if (r.getHoraRegistro().after(rc.getHoraInicio()) && r.getHoraRegistro().before(rc.getHoraFin())) {
                            cometeInfraccion = false;
                            break;
                        }
                    }

                    if (cometeInfraccion) {
                        RegistroInfraccion nuevaInfraccion = new RegistroInfraccion();
                        nuevaInfraccion.setRegistroAgenteTransito(r);
                        this.save(nuevaInfraccion);
                        result.add(nuevaInfraccion);
                        multaRealizada = true;

                    }
                }

            }

        }

        return result;
    }

}
