package alessiopanconi.u5w3d1.DTO;

import jakarta.validation.constraints.NotEmpty;

public record NewViaggioDTO(

        @NotEmpty(message =  "La destinazione è obbligatorio")
        String destinazione,
        @NotEmpty(message =  "La dataDiRichiesta è obbligatorio")
        String data
) {}
