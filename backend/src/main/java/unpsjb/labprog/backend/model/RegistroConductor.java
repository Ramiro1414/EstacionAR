package unpsjb.labprog.backend.model;

import java.sql.Timestamp;

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
public class RegistroConductor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String patente;

    private Timestamp horaInicio;

    private Timestamp horaFin;

    public String getPatente() {
        return this.patente;
    }

    public Timestamp getHoraInicio() {
        return this.horaInicio;
    }

    public Timestamp getHoraFin() {
        return this.horaFin;
    }
}
