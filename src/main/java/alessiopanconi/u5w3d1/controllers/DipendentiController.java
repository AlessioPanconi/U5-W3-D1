package alessiopanconi.u5w3d1.controllers;

import alessiopanconi.u5w3d1.DTO.NewDipendenteDTO;
import alessiopanconi.u5w3d1.entities.Dipendente;
import alessiopanconi.u5w3d1.exceptions.ValidationException;
import alessiopanconi.u5w3d1.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Dipendente> getDipendenti(@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int pageSize)
    {return  this.dipendentiService.findAll(pageNumber, pageSize);}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente getDipendenteById(@PathVariable long dipendenteId)
    {
        return this.dipendentiService.findDipendenteById(dipendenteId);

    }

    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findDipendenteByIdAndDelete(@PathVariable long dipendenteId) {
        this.dipendentiService.findDipendenteByIdAndDelete(dipendenteId);
    }

    //*********** ME ENDPOINTS ***********
    @GetMapping("/me")
    public Dipendente trovaIlMioProfilo(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente)
    {
        return this.dipendentiService.findDipendenteById(currentAuthenticatedDipendente.getIdDipendente());
    }

    @PutMapping("/me")
    public Dipendente modificaIlMioProfilo(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente, @RequestBody @Validated NewDipendenteDTO payload)
    {
        return this.dipendentiService.findDipendenteByIdAndUpdate(currentAuthenticatedDipendente.getIdDipendente(),payload);
    }

    @DeleteMapping("/me")
    public void eliminaIlMioProfilo(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente)
    {
            this.dipendentiService.findDipendenteByIdAndDelete(currentAuthenticatedDipendente.getIdDipendente());
    }


//    @PatchMapping("/{dipendenteId}/avatar")
//    public String uploadImage(@PathVariable long dipendenteId,@RequestParam("avatar") MultipartFile file) {
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getSize());
//        return this.dipendentiService.uploadAvatar(dipendenteId, file);
//    }
}
