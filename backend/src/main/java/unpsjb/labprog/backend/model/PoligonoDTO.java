package unpsjb.labprog.backend.model;

import java.util.Collection;

public class PoligonoDTO {
    
    private int id;

    private String nombre;

    private double precio;

    private Collection<PolylineData> polylinesList;

    public int getId() {
        return this.id;
    }

    public double getPrecio() {
        return this.precio;
    }

    public Collection<PolylineData> getPolylinesList() {
        return this.polylinesList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setPolylinesList(Collection<PolylineData> polylinesList) {
        this.polylinesList = polylinesList;
    }
}
