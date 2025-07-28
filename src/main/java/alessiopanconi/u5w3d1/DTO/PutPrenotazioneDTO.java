package alessiopanconi.u5w3d1.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PutPrenotazioneDTO(
        @Size(max = 150, message ="Il contenuto può essere lungo massimo 150 caratteri" )
        String preferenze,
        @NotNull(message = "La prenotazione deve essere associata ad un viaggio")
        long idViaggio
) {}
