package unpsjb.labprog.backend.presenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.RegistroInfraccionService;
import unpsjb.labprog.backend.model.RegistroAgenteTransito;

@RestController
@RequestMapping("registros-infracciones")
public class RegistroInfraccionPresenter {
    
    @Autowired
    RegistroInfraccionService service;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/cruzamiento",method=RequestMethod.GET)
    public ResponseEntity<Object> cruzamiento() {
        return Response.ok(service.cruzamientoNuevo(), "Proceso de cruzamiento de datos");
    }

    @RequestMapping(value = "/agruparPorPatente",method=RequestMethod.GET)
    public ResponseEntity<Object> cruzamiento(@RequestBody List<RegistroAgenteTransito> registrosAgenteTransito) {
        return Response.ok(service.agruparPorPatente(registrosAgenteTransito), "Proceso de agrupamiento de registros de agente de transito por patente");
    }

}