package alessiopanconi.u5w3d1.repositories;

import alessiopanconi.u5w3d1.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    @Query("SELECT COUNT(p) FROM Prenotazione p WHERE p.dipendente.id = :dipendenteId AND p.viaggio.data = :data")
    long countByDipendenteIdAndViaggioData(Long dipendenteId, LocalDate data);
}
