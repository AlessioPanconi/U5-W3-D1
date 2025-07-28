package alessiopanconi.u5w3d1.services;

import alessiopanconi.u5w3d1.DTO.NewViaggioDTO;
import alessiopanconi.u5w3d1.DTO.PatchViaggioDTO;
import alessiopanconi.u5w3d1.entities.Viaggio;
import alessiopanconi.u5w3d1.enums.Stato;
import alessiopanconi.u5w3d1.exceptions.BadRequestException;
import alessiopanconi.u5w3d1.exceptions.NotFoundException;
import alessiopanconi.u5w3d1.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class ViaggiService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    public Page<Viaggio> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.viaggioRepository.findAll(pageable);
    }

    public Viaggio saveViaggio(NewViaggioDTO payload)
    {
        try {
            LocalDate data = LocalDate.parse(payload.data());
            Stato statoViaggio;
            if (data.isBefore(LocalDate.now())|| data.isEqual(LocalDate.now())) {
                statoViaggio = Stato.COMPLETATO;
            }else {
                statoViaggio = Stato.IN_PROGRAMMA;
            }
            Viaggio newViaggio = new Viaggio(payload.destinazione(),data,statoViaggio);
            Viaggio savedViaggio = this.viaggioRepository.save(newViaggio);
            System.out.println("Il viaggio per: "+ payload.destinazione() +" è stato salvato correttamente");
            return savedViaggio;
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }
    }

    public Viaggio findViaggioById(long viaggioId) {
        return this.viaggioRepository.findById(viaggioId).orElseThrow(()-> new NotFoundException(viaggioId));
    }

    public Viaggio findViaggiByIdAndUpdate(long viaggioId, NewViaggioDTO payload) {
        try {
            LocalDate data = LocalDate.parse(payload.data());
            Viaggio found= findViaggioById(viaggioId);
            Stato statoViaggio;
            if (data.isBefore(LocalDate.now()) || data.isEqual(LocalDate.now())) {
                statoViaggio = Stato.COMPLETATO;
            }else {
                statoViaggio = Stato.IN_PROGRAMMA;
            }
            found.setData(data);
            found.setStato(statoViaggio);
            found.setDestinazione(payload.destinazione());
            Viaggio viaggioModificato = this.viaggioRepository.save(found);
            System.out.println("Viaggio modificato correttamente");
            return viaggioModificato;
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della dataDiRichiesta inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }
    }

    public void findViaggioByIdAndDelete(long viaggioId) {
        Viaggio found= findViaggioById(viaggioId);
        this.viaggioRepository.delete(found);
    }

    public Viaggio findVianggioByIdAndModifyStato(long viaggioId, PatchViaggioDTO payload){
        Viaggio found= findViaggioById(viaggioId);

        String stato = payload.stato().toUpperCase();
        if (stato.equals("IN_PROGRAMMA")) {
            if (found.getData().isAfter(LocalDate.now()))
            {
                found.setStato(Stato.IN_PROGRAMMA);
                Viaggio viaggioModificato = this.viaggioRepository.save(found);
                System.out.println("Viaggio modificato correttamente");
                return viaggioModificato;
            }else{
                throw new BadRequestException("Non puoi mettere come stato IN_PROGRAMMA ad un viaggio avvenuto nel passato");
            }
        } else if (stato.equals("COMPLETATO")) {
            if (found.getData().isBefore(LocalDate.now()) || found.getData().isEqual(LocalDate.now()))
            {
                found.setStato(Stato.COMPLETATO);
                Viaggio viaggioModificato = this.viaggioRepository.save(found);
                System.out.println("Viaggio modificato correttamente");
                return viaggioModificato;
            }else{
                throw new BadRequestException("Non puoi mettere come stato COMPLETATO ad un viaggio che non è ancora stato fatto");
            }
        } else {
            throw new BadRequestException("Lo stato che hai inserito non è valido, gli unici stati ammessi sono (completato) (in_programma)");
        }
    }
}
