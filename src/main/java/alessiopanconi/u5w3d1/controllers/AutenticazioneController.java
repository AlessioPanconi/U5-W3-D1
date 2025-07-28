package alessiopanconi.u5w3d1.controllers;

import alessiopanconi.u5w3d1.DTO.DipendenteLoginDTO;
import alessiopanconi.u5w3d1.DTO.LoginRespDTO;
import alessiopanconi.u5w3d1.DTO.NewDipendenteDTO;
import alessiopanconi.u5w3d1.entities.Dipendente;
import alessiopanconi.u5w3d1.exceptions.ValidationException;
import alessiopanconi.u5w3d1.services.AutenticazioneService;
import alessiopanconi.u5w3d1.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticazione")
public class AutenticazioneController {

    @Autowired
    private AutenticazioneService autenticazioneService;

    @Autowired
    private DipendentiService dipendentiService;

    @PostMapping("/login")
    public LoginRespDTO loginRespDTO(@RequestBody DipendenteLoginDTO body)
    {
        String token = autenticazioneService.checkAccessAndGenerateToken(body);
        return new LoginRespDTO(token);
    }

    @PostMapping("/RegisterDipendente")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente createDipendente(@RequestBody @Validated NewDipendenteDTO body, BindingResult validationResult)
    {
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else{
            return this.dipendentiService.saveDipendente(body);
        }
    }
}
