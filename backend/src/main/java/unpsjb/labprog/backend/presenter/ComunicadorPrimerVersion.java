package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.PatronesPatentesService;
import unpsjb.labprog.backend.business.RegistroAgenteTransitoService;
import unpsjb.labprog.backend.business.RegistroConductorService;
import unpsjb.labprog.backend.model.RegistroAgenteTransito;
import unpsjb.labprog.backend.model.RegistroConductor;

@RestController
@RequestMapping("/datos")
public class ComunicadorPrimerVersion {

    @Autowired
    RegistroConductorService registroConductorService;

    @Autowired
    RegistroAgenteTransitoService registroAgenteTransitoService;

    @Autowired
    PatronesPatentesService patronesPatentesService;

    @RequestMapping(value = "/actualizar", method = RequestMethod.GET)
    public ResponseEntity<Object> enviarDatos() {
        return Response.ok("Datos del sistema central");
    }

    @RequestMapping(value = "/registrar/conductor", method = RequestMethod.POST)
    public ResponseEntity<Object> recibirDatosConductor(@RequestBody RegistroConductor unRegistroConductor) {
        return Response.ok(registroConductorService.save(unRegistroConductor), "Registro de conductor recibido");
    }

    @RequestMapping(value = "/registrar/obleista", method = RequestMethod.POST)
    public ResponseEntity<Object> recibirDatosObleista(@RequestBody RegistroAgenteTransito unRegistroAgenteTransito) {
        return Response.ok(registroAgenteTransitoService.save(unRegistroAgenteTransito),
                "Registro de obleista recibido");
    }

    @RequestMapping(value = "/patrones", method=RequestMethod.GET)
    public ResponseEntity<Object> actualizarPatrones() {
        return Response.ok(patronesPatentesService.findAll(), "todo bien");
    }

}
