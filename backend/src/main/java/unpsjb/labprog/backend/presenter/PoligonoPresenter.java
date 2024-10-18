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
import unpsjb.labprog.backend.business.PoligonoService;
import unpsjb.labprog.backend.model.Poligono;

@RestController
@RequestMapping("/poligonos")
public class PoligonoPresenter {
    
    @Autowired
    PoligonoService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") int id) {
        Poligono unPoligonoOrNull = service.findById(id);
        return (unPoligonoOrNull != null) ? Response.ok(unPoligonoOrNull)
                : Response.notFound("error");
    }

    /*
    @RequestMapping(value = "/dentro-de-poligono/{latitud},{longitud}", method = RequestMethod.GET)
    public ResponseEntity<Object> dentroDePoligono(@PathVariable("latitud") double latitud, @PathVariable("longitud") double longitud) {

        double[] coords = new double[2];
        coords[0] = latitud;
        coords[1] = longitud;

        return Response.ok(service.dentroDePoligono(coords));
    }
    */

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by("id").ascending();
        return Response.ok(service.findByPage(page, size, sort)); 
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        service.delete(id);
        return Response.ok("Poligono de estacionamiento " + id + " borrado con éxito.");
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Object> crearPoligono(@RequestBody Poligono poligono) {
        return Response.ok(service.save(poligono));
    }

    
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Poligono poligono) {
        if (poligono.getId() <= 0) {
            return Response.error(
                    poligono,
                    "error");
        }
        service.delete(poligono.getId());
        return Response.ok(service.save(poligono));
    }
    
        /*
    @RequestMapping(value = "/id/{id}/lineas-poligono", method=RequestMethod.GET)
    public ResponseEntity<Object> getPoligonoCompleto(@PathVariable("id") int id) {
        return Response.ok(service.getPoligonoCompleto(id));
    }
        */
}
