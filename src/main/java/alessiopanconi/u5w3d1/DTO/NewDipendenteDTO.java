package alessiopanconi.u5w3d1.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewDipendenteDTO(

        @NotEmpty(message =  "L'username è obbligatorio")
        String username,
        @NotEmpty(message =  "Il nome è obbligatorio")
        String nome,
        @NotEmpty(message =  "Il cognome è obbligatorio")
        String cognome,
        @NotEmpty(message = "L'indirizzo email è obbligatorio")
        @Email(message = "L'indirizzo email inserito non è nel formato giusto")
        String email,
        @NotEmpty(message = "La password è obbligatoria")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{4,}$", message = "La password deve contenere: 1 carat maiuscolo, uno minuscolo.....")
        @Size(min = 6)
        String password
) {}
