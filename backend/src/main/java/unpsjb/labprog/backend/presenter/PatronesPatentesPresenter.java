package unpsjb.labprog.backend.presenter;

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
import unpsjb.labprog.backend.business.PatronesPatentesService;
import unpsjb.labprog.backend.model.PatronPatente;

@RestController
@RequestMapping("/patrones/patentes")
public class PatronesPatentesPresenter {

    @Autowired
    PatronesPatentesService service;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody PatronPatente aPatronPatente) {
        if (aPatronPatente.getId() != 0) {
            return Response.error(
                    aPatronPatente,
                    "Esta intentando crear un Patron de Patente. Este no puede tener un id definido.");
        }

        PatronPatente patronPatente = aPatronPatente;
       
            patronPatente = service.save(aPatronPatente);
       
        return Response.ok(patronPatente,"Patron patente " + patronPatente.getExpresionRegularPatente() + " registrado correctamente");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") int id) {
        PatronPatente aCustomerOrNull = service.findById(id);
        return (aCustomerOrNull != null) ? Response.ok(aCustomerOrNull)
                : Response.notFound("error");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody PatronPatente aPatronPatente) {
        if (aPatronPatente.getId() <= 0) {
            return Response.error(
                    aPatronPatente,
                    "error");
        }

        PatronPatente patronPatente = aPatronPatente;
        patronPatente = service.save(aPatronPatente);
        return Response.ok(patronPatente);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        service.delete(id);
        return Response.ok("Patron patente " + id + " borrado con éxito.");
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by("id").ascending();
        return Response.ok(service.findByPage(page, size, sort)); 
    }

}
