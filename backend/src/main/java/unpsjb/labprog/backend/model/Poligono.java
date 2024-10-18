package unpsjb.labprog.backend.model;

import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Poligono {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double precio;

    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Punto> puntos;
}
