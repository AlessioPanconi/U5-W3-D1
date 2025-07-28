package alessiopanconi.u5w3d1.DTO;

import jakarta.validation.constraints.NotEmpty;

public record PatchViaggioDTO(
        @NotEmpty(message =  "Lo stato è obbligatorio")
                              String stato
)
{}
