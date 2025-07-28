package alessiopanconi.u5w3d1.services;

import alessiopanconi.u5w3d1.DTO.NewPrenotazioneDTO;
import alessiopanconi.u5w3d1.DTO.PutPrenotazioneDTO;
import alessiopanconi.u5w3d1.entities.Dipendente;
import alessiopanconi.u5w3d1.entities.Prenotazione;
import alessiopanconi.u5w3d1.entities.Viaggio;
import alessiopanconi.u5w3d1.enums.Stato;
import alessiopanconi.u5w3d1.exceptions.BadRequestException;
import alessiopanconi.u5w3d1.exceptions.NotFoundException;
import alessiopanconi.u5w3d1.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PrenotazioniService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private DipendentiService dipendentiService;

    @Autowired
    private ViaggiService viaggiService;

    public Page<Prenotazione> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione savePrenotazione(NewPrenotazioneDTO payload)
    {
            Dipendente dipendente = this.dipendentiService.findDipendenteById(payload.idDipendente());
            Viaggio viaggio = this.viaggiService.findViaggioById(payload.idViaggio());

            if(viaggio.getStato() == Stato.COMPLETATO)
            {
                throw new BadRequestException("Non puoi creare una prenotazione per un viaggio già completato");
            }
            if (this.prenotazioneRepository.countByDipendenteIdAndViaggioData(dipendente.getIdDipendente(), viaggio.getData())!=0)
            {
                throw new BadRequestException("Il dipendente: "+dipendente.getNome()+" "+dipendente.getCognome() +" ha già una prenotazione per il giorno " + viaggio.getData());
            }
            Prenotazione newPrenotazione = new Prenotazione(LocalDate.now(),payload.preferenze(),dipendente,viaggio);
            Prenotazione savedPrenotazione = this.prenotazioneRepository.save(newPrenotazione);
            System.out.println("La prenotazione di: "+ dipendente.getNome()+" "+dipendente.getCognome() +" è stata salvata correttamente");
            return savedPrenotazione;

    }
//non l'ho testato non so se funziona
    public Prenotazione findPrenotazioneByIdAndUpdate(long prenotazioneId, PutPrenotazioneDTO payload)
    {
        Prenotazione found = this.findPrenotazioneById(prenotazioneId);
        Viaggio viaggio = this.viaggiService.findViaggioById(payload.idViaggio());
        if(viaggio.getStato() == Stato.COMPLETATO)
        {
            throw new BadRequestException("Non puoi modificare una prenotazione con un viaggio già completato");
        }
        if (this.prenotazioneRepository.countByDipendenteIdAndViaggioData(found.getDipendente().getIdDipendente(), viaggio.getData())!=0)
        {
            throw new BadRequestException("Il dipendente: "+found.getDipendente().getNome()+" "+found.getDipendente().getCognome() +" ha già una prenotazione per il giorno " + viaggio.getData());
        }
        found.setDataDiRichiesta(LocalDate.now());
        found.setPreferenze(payload.preferenze());
        found.setViaggio(viaggio);
        Prenotazione prenotazioneModificata = this.prenotazioneRepository.save(found);
        System.out.println("Prenotazione modificata correttamente");
        return prenotazioneModificata;
    }

    public Prenotazione findPrenotazioneById(long prenotazioneId) {
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(()-> new NotFoundException(prenotazioneId));
    }

    public void findPrenotazioneByIdAndDelete(long prenotazioneId) {
        Prenotazione found= findPrenotazioneById(prenotazioneId);
        this.prenotazioneRepository.delete(found);
    }
}