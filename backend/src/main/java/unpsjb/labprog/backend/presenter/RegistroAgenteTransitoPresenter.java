package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.RegistroAgenteTransitoService;

@RestController
@RequestMapping("registros-agentes-transito")
public class RegistroAgenteTransitoPresenter {
    
    @Autowired
    RegistroAgenteTransitoService service;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }
}
