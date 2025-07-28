package alessiopanconi.u5w3d1.entities;

import alessiopanconi.u5w3d1.enums.Stato;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "viaggio")
@Getter
@Setter
@NoArgsConstructor
public class Viaggio {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long idViaggio;
    private String destinazione;
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    private Stato stato;

    public Viaggio(String destinazione, LocalDate data, Stato stato) {
        this.destinazione = destinazione;
        this.data = data;
        this.stato = stato;
    }
    @Override
    public String toString() {
        return "Viaggio{" +
                "idPrenotazione=" + idViaggio +
                ", destinazione='" + destinazione + '\'' +
                ", dataDiRichiesta=" + data +
                ", stato=" + stato +
                '}';
    }
}
