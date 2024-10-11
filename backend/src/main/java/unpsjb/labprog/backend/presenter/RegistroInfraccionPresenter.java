package unpsjb.labprog.backend.presenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.RegistroAgenteTransitoService;
import unpsjb.labprog.backend.business.RegistroInfraccionService;
import unpsjb.labprog.backend.model.RegistroAgenteTransito;
import unpsjb.labprog.backend.model.RegistroInfraccion;

@RestController
@RequestMapping("registros-infracciones")
public class RegistroInfraccionPresenter {
    
    @Autowired
    RegistroInfraccionService service;
    @Autowired
    RegistroAgenteTransitoService registroAgenteTransitoService;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") int id) {
        RegistroInfraccion unRegistroInfraccionOrNull = service.findById(id);
        return (unRegistroInfraccionOrNull != null) ? Response.ok(unRegistroInfraccionOrNull)
                : Response.notFound("error");
    }

    @RequestMapping(value = "/detalle/registro-agente-transito/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> detalleRegistroAgenteTransito(@PathVariable("id") int id) {
        RegistroAgenteTransito unRegistroAgenteTransitoOrNull = registroAgenteTransitoService.findById(id);
        return (unRegistroAgenteTransitoOrNull != null) ? Response.ok(unRegistroAgenteTransitoOrNull)
                : Response.notFound("error");
    }

    @RequestMapping(value = "/cruzamiento",method=RequestMethod.GET)
    public ResponseEntity<Object> cruzamiento() {
        return Response.ok(service.cruzamientoNuevo(), "Proceso de cruzamiento de datos");
    }

    @RequestMapping(value = "/agruparPorPatente",method=RequestMethod.GET)
    public ResponseEntity<Object> cruzamiento(@RequestBody List<RegistroAgenteTransito> registrosAgenteTransito) {
        return Response.ok(service.agruparPorPatente(registrosAgenteTransito), "Proceso de agrupamiento de registros de agente de transito por patente");
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by("id").ascending();
        return Response.ok(service.findByPage(page, size, sort)); 
    }

}