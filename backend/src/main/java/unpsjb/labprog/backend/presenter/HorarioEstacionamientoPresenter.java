package unpsjb.labprog.backend.presenter;

import java.time.LocalTime;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
import unpsjb.labprog.backend.business.HorarioEstacionamientoService;
import unpsjb.labprog.backend.model.HoraInicioHoraFin;
import unpsjb.labprog.backend.model.HorarioEstacionamiento;

@RestController
@RequestMapping("/horarios/estacionamiento")
public class HorarioEstacionamientoPresenter {

    @Autowired
    HorarioEstacionamientoService service;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody HorarioEstacionamiento aHorarioEstacionamiento) {
        if (aHorarioEstacionamiento.getId() != 0) {
            return Response.error(
                aHorarioEstacionamiento,
                    "Esta intentando crear un Patron de Patente. Este no puede tener un id definido.");
        }
        
        return Response.ok(service.save(aHorarioEstacionamiento),"Horario estacionamiento registrado correctamente: " + aHorarioEstacionamiento.toString());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/horariosDia/{id}",method = RequestMethod.GET)
    public ResponseEntity<Object> findHorariosDia(@PathVariable("id") int id) {
        return Response.ok(service.findHorariosDelDia(id));
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") int id) {
        HorarioEstacionamiento aHorarioEstacionamiento = service.findById(id);
        
        Collection<HoraInicioHoraFin> horarios = aHorarioEstacionamiento.getHorariosDelDia();
        
        List<HoraInicioHoraFin> listaHorarios = new ArrayList<>(horarios);
        
        listaHorarios.sort(Comparator.comparing(HoraInicioHoraFin::getHoraInicio));
        
        aHorarioEstacionamiento.setHorariosDelDia(listaHorarios);
        
        return (aHorarioEstacionamiento != null) ? Response.ok(aHorarioEstacionamiento)
                : Response.notFound("error");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody HorarioEstacionamiento aHorarioEstacionamiento) {
        if (aHorarioEstacionamiento.getId() <= 0) {
            return Response.error(
                aHorarioEstacionamiento,
                    "error");
        }

        HorarioEstacionamiento horarioEstacionamiento = aHorarioEstacionamiento;
        horarioEstacionamiento = service.save(aHorarioEstacionamiento);
        return Response.ok(horarioEstacionamiento, "Horario estacionamiento actualizado correctamente: " + horarioEstacionamiento.toString());
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
        Sort sort = Sort.by("nombre").ascending();
        return Response.ok(service.findByPage(page, size, sort)); 
    }

}
