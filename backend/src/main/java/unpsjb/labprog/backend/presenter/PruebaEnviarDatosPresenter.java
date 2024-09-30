package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.RegistroConductorService;
import unpsjb.labprog.backend.model.RegistroConductor;

@RestController
@RequestMapping("/datos")
public class PruebaEnviarDatosPresenter {

    @Autowired
    RegistroConductorService registroConductorService;
    
    @RequestMapping(value = "/actualizar", method=RequestMethod.GET)
    public ResponseEntity<Object> enviarDatos() {
        return Response.ok("Datos del sistema central");
    }

    @RequestMapping(value = "/registrar", method=RequestMethod.POST)
    public ResponseEntity<Object> recibirDatos(@RequestBody RegistroConductor unRegistroConductor) {
        return Response.ok(registroConductorService.save(unRegistroConductor), "Registro de conductor recibido");
    }

}
