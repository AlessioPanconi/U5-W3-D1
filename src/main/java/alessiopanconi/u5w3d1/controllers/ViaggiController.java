package alessiopanconi.u5w3d1.controllers;

import alessiopanconi.u5w3d1.DTO.NewViaggioDTO;
import alessiopanconi.u5w3d1.DTO.PatchViaggioDTO;
import alessiopanconi.u5w3d1.entities.Viaggio;
import alessiopanconi.u5w3d1.exceptions.ValidationException;
import alessiopanconi.u5w3d1.services.ViaggiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/viaggi")
public class ViaggiController {

    @Autowired
    private ViaggiService viaggiService;

    @GetMapping
    public Page<Viaggio> getViaggi(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize)
    {return  this.viaggiService.findAll(pageNumber, pageSize);}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio createViaggio(@RequestBody @Validated NewViaggioDTO body, BindingResult validationResult)
    {
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else{
            return this.viaggiService.saveViaggio(body);
        }
    }

    @GetMapping("/{viaggioId}")
    public Viaggio getViaggioById(@PathVariable long viaggioId)
    {
        return this.viaggiService.findViaggioById(viaggioId);
    }

    @PutMapping("/{viaggioId}")
    public Viaggio findViaggioByIdAndUpdate (@RequestBody @Validated NewViaggioDTO body, BindingResult validationResult , @PathVariable long viaggioId)
    {
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else{
            return this.viaggiService.findViaggiByIdAndUpdate(viaggioId,body);
        }
    }

    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findViaggioByIdAndDelete(@PathVariable long viaggioId) {
        this.viaggiService.findViaggioByIdAndDelete(viaggioId);
    }

    @PatchMapping("/{viaggioId}/stato")
    public Viaggio modificaStatoViaggio(@PathVariable long viaggioId, @RequestBody PatchViaggioDTO payload) {
        return this.viaggiService.findVianggioByIdAndModifyStato(viaggioId, payload);
    }

}
