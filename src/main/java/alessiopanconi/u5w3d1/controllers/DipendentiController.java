package alessiopanconi.u5w3d1.controllers;

import alessiopanconi.u5w3d1.DTO.NewDipendenteDTO;
import alessiopanconi.u5w3d1.entities.Dipendente;
import alessiopanconi.u5w3d1.exceptions.ValidationException;
import alessiopanconi.u5w3d1.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {

    @Autowired
    private DipendentiService dipendentiService;

    @GetMapping
    public Page<Dipendente> getDipendenti(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {return  this.dipendentiService.findAll(pageNumber, pageSize);}

    @PostMapping
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

    @GetMapping("/{dipendenteId}")
    public Dipendente getDipendenteById(@PathVariable long dipendenteId)
    {
        return this.dipendentiService.findDipendenteById(dipendenteId);

    }

    @PutMapping("/{dipendenteId}")
    public Dipendente findDipendenteByIdAndUpdate (@RequestBody @Validated NewDipendenteDTO body, BindingResult validationResult , @PathVariable long dipendenteId)
    {
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else{
            return this.dipendentiService.findDipendenteByIdAndUpdate(dipendenteId,body);
        }
    }

    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findDipendenteByIdAndDelete(@PathVariable long dipendenteId) {
        this.dipendentiService.findDipendenteByIdAndDelete(dipendenteId);
    }

    @PatchMapping("/{dipendenteId}/avatar")
    public String uploadImage(@PathVariable long dipendenteId,@RequestParam("avatar") MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        return this.dipendentiService.uploadAvatar(dipendenteId, file);
    }
}
