package alessiopanconi.u5w3d1.DTO;

import java.time.LocalDateTime;

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp)
{}
