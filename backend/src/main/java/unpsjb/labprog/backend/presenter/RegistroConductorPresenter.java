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
@RequestMapping("registros-conductores")
public class RegistroConductorPresenter {
    
    @Autowired
    RegistroConductorService service;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/recibir", method=RequestMethod.POST)
    public ResponseEntity<Object> recibirRegistro(@RequestBody RegistroConductor unRegistroConductor) {

        return Response.ok(service.save(unRegistroConductor), "Registro de conductor recibido");
    }
}
