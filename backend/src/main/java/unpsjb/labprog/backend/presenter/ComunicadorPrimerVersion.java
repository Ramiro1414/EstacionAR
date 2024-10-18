package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.HorarioEstacionamientoService;
import unpsjb.labprog.backend.business.PatronesPatentesService;
import unpsjb.labprog.backend.business.PoligonoService;
import unpsjb.labprog.backend.business.RegistroAgenteTransitoService;
import unpsjb.labprog.backend.business.RegistroConductorService;
import unpsjb.labprog.backend.model.RegistroAgenteTransito;
import unpsjb.labprog.backend.model.RegistroConductor;

@RestController
@RequestMapping("/comunicador")
public class ComunicadorPrimerVersion {

    @Autowired
    RegistroConductorService registroConductorService;

    @Autowired
    RegistroAgenteTransitoService registroAgenteTransitoService;

    @Autowired
    PatronesPatentesService patronesPatentesService;

    @Autowired
    PoligonoService poligonoService;

    @Autowired
    HorarioEstacionamientoService horarioEstacionamientoService;


    @RequestMapping(value = "/actualizar", method = RequestMethod.GET)
    public ResponseEntity<Object> enviarDatos() {
        return Response.ok("Datos del sistema central");
    }

    @RequestMapping(value = "/registrar/conductor", method = RequestMethod.POST)
    public ResponseEntity<Object> recibirDatosConductor(@RequestBody RegistroConductor unRegistroConductor) {
        return Response.ok(registroConductorService.save(unRegistroConductor), "Registro de conductor recibido");
    }

    @RequestMapping(value = "/registrar/conductor-sin-aplicacion", method = RequestMethod.POST)
    public ResponseEntity<Object> recibirDatosConductorSinAplicacion(@RequestBody RegistroConductor unRegistroConductor) {
        return Response.ok(registroConductorService.save(unRegistroConductor), "Registro de conductor sin aplicacion recibido");
    }

    @RequestMapping(value = "/registrar/obleista", method = RequestMethod.POST)
    public ResponseEntity<Object> recibirDatosObleista(@RequestBody RegistroAgenteTransito unRegistroAgenteTransito) {
        return Response.ok(registroAgenteTransitoService.save(unRegistroAgenteTransito),
                "Registro de obleista recibido");
    }

    @RequestMapping(value = "/actualizar/patrones", method=RequestMethod.GET)
    public ResponseEntity<Object> actualizarPatrones() {
        return Response.ok(patronesPatentesService.findAll(), "todo bien");
    }
    
    @RequestMapping(value = "/actualizar/poligonos", method=RequestMethod.GET)
    public ResponseEntity<Object> actualizarPoligonos() {
        return Response.ok(poligonoService.findAll(), "todo bien");
    }

    @RequestMapping(value = "/actualizar/horarios", method=RequestMethod.GET)
    public ResponseEntity<Object> actualizarHorarios() {
        return Response.ok(horarioEstacionamientoService.findAll(), "todo bien");
    }

}
