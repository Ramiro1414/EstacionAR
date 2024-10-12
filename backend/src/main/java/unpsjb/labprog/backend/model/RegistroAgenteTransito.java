package unpsjb.labprog.backend.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RegistroAgenteTransito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Timestamp horaRegistro;

    private String patente;

    private double latitud;

    private double longitud;

    @Column(columnDefinition = "BYTEA")
    private byte[] foto;

    public String getPatente() {
        return this.patente;
    }

    public Timestamp getHoraRegistro() {
        return this.horaRegistro;
    }

    public double getLatitud() {
        return this.latitud;
    }

    public double getLongitud() {
        return this.longitud;
    }
}
