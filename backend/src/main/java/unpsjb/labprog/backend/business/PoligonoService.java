package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.LineaPoligono;
import unpsjb.labprog.backend.model.Poligono;
import unpsjb.labprog.backend.model.PoligonoDTO;
import unpsjb.labprog.backend.model.PolylineData;
import unpsjb.labprog.backend.model.Punto;

@Service
public class PoligonoService {

    private final static double MAX_DISTANCIA_A_LINEA_POLIGONO = 10.0;

    @Autowired
    PoligonoRepository repository;
    @Autowired
    PuntoRepository puntoRepository;
    @Autowired
    LineaPoligonoRepository lineaPoligonoRepository;

    public List<Poligono> findAll() {
        List<Poligono> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public Poligono findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Poligono save(Poligono unPoligono) {
        return repository.save(unPoligono);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Poligono> findByPage(int page, int size, Sort sort) {
        return repository.findAll(PageRequest.of(page, size, sort));
    }

    public Poligono crearPoligono(PoligonoDTO poligonoDTO) {

        Poligono nuevoPoligono = new Poligono();

        nuevoPoligono.setPrecio(poligonoDTO.getPrecio());
        nuevoPoligono.setNombre(poligonoDTO.getNombre());

        List<LineaPoligono> lineasPoligono = new ArrayList<>();

        for (PolylineData polylineData : poligonoDTO.getPolylinesList()) {
            Punto punto1 = new Punto();
            Punto punto2 = new Punto();

            punto1.setLatitud(polylineData.getCoords()[0]);
            punto1.setLongitud(polylineData.getCoords()[1]);

            punto2.setLatitud(polylineData.getCoords()[2]);
            punto2.setLongitud(polylineData.getCoords()[3]);

            punto1 = puntoRepository.save(punto1);
            punto2 = puntoRepository.save(punto2);

            LineaPoligono lineaPoligono = new LineaPoligono();

            lineaPoligono.setPunto1(punto1);
            lineaPoligono.setPunto2(punto2);

            lineaPoligonoRepository.save(lineaPoligono);
            lineasPoligono.add(lineaPoligono);

        }
        nuevoPoligono.setLineasPoligono(lineasPoligono);
        nuevoPoligono = repository.save(nuevoPoligono);

        return nuevoPoligono;
    }

    /**
     * Retorna el id del poligono al que pertenece las coordenadas que se pasan como
     * parametro. Retorna -1 si no pertenece a ningun poligono
     */
    public int dentroDePoligono(double[] coordenadas) {

        double latitud = coordenadas[0];
        double longitud = coordenadas[1];

        List<LineaPoligono> lineasPoligonos = null;

        List<Poligono> poligonos = (List<Poligono>) repository.findAll();

        for (Poligono poligono : poligonos) {

            lineasPoligonos = (List<LineaPoligono>) poligono.getLineasPoligono();

            for (LineaPoligono lineaPoligono : lineasPoligonos) {

                double[] punto1 = { lineaPoligono.getPunto1().getLatitud(), lineaPoligono.getPunto1().getLongitud() };
                double[] punto2 = { lineaPoligono.getPunto2().getLatitud(), lineaPoligono.getPunto2().getLongitud() };

                // Calcula la distancia desde el punto a la línea
                double distancia = calcularDistanciaDesdePuntoALinea(latitud, longitud, punto1, punto2);

                // Verifica si la distancia es menor o igual a 5 metros
                if (distancia <= MAX_DISTANCIA_A_LINEA_POLIGONO) {
                    return poligono.getId(); // Retorna el ID del polígono si el punto está dentro de la línea
                }
            }
        }

        // no esta en ningun poligono
        return -1;

    }

    /** Método para calcular la distancia desde un punto a una línea */
    private double calcularDistanciaDesdePuntoALinea(double lat, double lon, double[] punto1, double[] punto2) {
        // Convertir latitud y longitud a radianes
        double latRad = Math.toRadians(lat);
        double lonRad = Math.toRadians(lon);
        double lat1Rad = Math.toRadians(punto1[0]);
        double lon1Rad = Math.toRadians(punto1[1]);
        double lat2Rad = Math.toRadians(punto2[0]);
        double lon2Rad = Math.toRadians(punto2[1]);

        // Calcular la distancia usando la fórmula de distancia entre un punto y una
        // línea
        double a = lat1Rad - lat2Rad;
        double b = lon1Rad - lon2Rad;

        double distancia = Math.abs(a * (lon1Rad - lonRad) - b * (lat1Rad - latRad)) / Math.sqrt(a * a + b * b);

        // Convertir la distancia de radianes a metros (suponiendo una esfera de la
        // Tierra)
        // 1 radian = 6371000 metros (radio de la Tierra)
        distancia *= 6371000;

        return distancia;
    }

    public PoligonoDTO getPoligonoCompleto(int id) {

        PoligonoDTO nuevoPoligonoDTO = new PoligonoDTO();

        Poligono poligono = repository.findById(id).orElse(null);

        nuevoPoligonoDTO.setId(id);
        nuevoPoligonoDTO.setPrecio(poligono.getPrecio());

        Collection<PolylineData> polylinesList = new ArrayList<PolylineData>();

        List<LineaPoligono> lineasPoligonos = (List<LineaPoligono>) repository.findById(id).get().getLineasPoligono();

        int idPolylineData = 0;

        for (LineaPoligono lineaPoligono : lineasPoligonos) {

            PolylineData nuevoPolylineData = new PolylineData();

            nuevoPolylineData.setId(idPolylineData);
            idPolylineData++;

            double[] coordsPolylineData = new double[4]; // son 4 valores, dos latitudes y dos longitudes. esto porque
                                                         // son dos puntos que conforman una linea.

            coordsPolylineData[0] = lineaPoligono.getPunto1().getLatitud();
            coordsPolylineData[1] = lineaPoligono.getPunto1().getLongitud();
            coordsPolylineData[2] = lineaPoligono.getPunto2().getLatitud();
            coordsPolylineData[3] = lineaPoligono.getPunto2().getLongitud();

            nuevoPolylineData.setCoords(coordsPolylineData);
            polylinesList.add(nuevoPolylineData);
        }

        nuevoPoligonoDTO.setPolylinesList(polylinesList);

        return nuevoPoligonoDTO;
    }
}