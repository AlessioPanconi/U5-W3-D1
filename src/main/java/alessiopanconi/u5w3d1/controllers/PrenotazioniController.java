package alessiopanconi.u5w3d1.controllers;

import alessiopanconi.u5w3d1.DTO.NewPrenotazioneDTO;
import alessiopanconi.u5w3d1.DTO.PutPrenotazioneDTO;
import alessiopanconi.u5w3d1.entities.Prenotazione;
import alessiopanconi.u5w3d1.exceptions.ValidationException;
import alessiopanconi.u5w3d1.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    @Autowired
    private PrenotazioniService prenotazioniService;

    @GetMapping
    public Page<Prenotazione> getPrenotazione(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize)
    {return  this.prenotazioniService.findAll(pageNumber, pageSize);}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione createPrenotazione(@RequestBody @Validated NewPrenotazioneDTO body, BindingResult validationResult)
    {
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else{
            return this.prenotazioniService.savePrenotazione(body);
        }
    }
//non test
    @PutMapping("/{prenotazioneId}")
    public Prenotazione findPrenotazioneByIdAndUpdate (@RequestBody @Validated PutPrenotazioneDTO body, BindingResult validationResult , @PathVariable long dipendenteId)
    {
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else{
            return this.prenotazioniService.findPrenotazioneByIdAndUpdate(dipendenteId,body);
        }
    }

    @GetMapping("/{prenotazioneId}")
    public Prenotazione getViaggioById(@PathVariable long prenotazioneId)
    {
        return this.prenotazioniService.findPrenotazioneById(prenotazioneId);
    }

    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findPrenotazioneByIdAndDelete(@PathVariable long prenotazioneId) {
        this.prenotazioniService.findPrenotazioneByIdAndDelete(prenotazioneId);
    }
}
