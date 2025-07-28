package alessiopanconi.u5w3d1.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "prenotazione")
@Getter
@Setter
@NoArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long idPrenotazione;
    @Column(name = "data_di_Richiesta")
    private LocalDate dataDiRichiesta;
    private String preferenze;

    @ManyToOne
    @JoinColumn(name = "id_dipendente", nullable = false)
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "id_viaggio", nullable = false)
    private Viaggio viaggio;

    public Prenotazione(LocalDate dataDiRichiesta, String preferenze, Dipendente dipendente, Viaggio viaggio) {
        this.dataDiRichiesta = dataDiRichiesta;
        this.preferenze = preferenze;
        this.dipendente = dipendente;
        this.viaggio = viaggio;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "idPrenotazione=" + idPrenotazione +
                ", dataDiRichiesta=" + dataDiRichiesta +
                ", preferenze='" + preferenze + '\'' +
                '}';
    }
}
