package alessiopanconi.u5w3d1.services;

import alessiopanconi.u5w3d1.DTO.NewDipendenteDTO;
import alessiopanconi.u5w3d1.entities.Dipendente;
import alessiopanconi.u5w3d1.exceptions.BadRequestException;
import alessiopanconi.u5w3d1.exceptions.NotFoundException;
import alessiopanconi.u5w3d1.repositories.DipendenteRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class DipendentiService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private Cloudinary getImageUploader;

    public Page<Dipendente> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.dipendenteRepository.findAll(pageable);
    }

    public Dipendente findByEmail(String email) {
        return this.dipendenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Il dipendente con l'email " + email + " non è stato trovato!"));
    }

    public Dipendente saveDipendente(NewDipendenteDTO payload)
    {
        this.dipendenteRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
            throw new BadRequestException("L'email " + dipendente.getEmail() + " appartiene già ad un'altro dipendente");
        });
        String avatarUrl = "https://ui-avatars.com/api/?name=" + payload.nome() + "+" + payload.cognome();
        Dipendente newDipendente = new Dipendente(payload.username(),payload.nome(),payload.cognome(),payload.email(),avatarUrl,payload.password());
        Dipendente savedDipendente = this.dipendenteRepository.save(newDipendente);
        System.out.println("Il dipendente: "+ payload.nome() +" "+payload.cognome() +" è stato salvato correttamente");
        return savedDipendente;
    }

    public Dipendente findDipendenteById(long dipendenteId) {
        return this.dipendenteRepository.findById(dipendenteId).orElseThrow(()-> new NotFoundException(dipendenteId));
    }

    public Dipendente findDipendenteByIdAndUpdate(long dipendenteId, NewDipendenteDTO payload) {
        Dipendente found= findDipendenteById(dipendenteId);
        if (!found.getEmail().equals(payload.email()))
            this.dipendenteRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
                throw new BadRequestException("L'email " + dipendente.getEmail() + " appartiene già ad un'altro autore");
            });

        found.setUsername(payload.username());
        found.setNome(payload.nome());
        found.setCognome(payload.cognome());
        found.setEmail(payload.email());
        found.setAvatarUrl("https://ui-avatars.com/api/?name=" + payload.nome() + "+" + payload.cognome());

        Dipendente dipendenteModified = this.dipendenteRepository.save(found);
        System.out.println("Dipendente modificato correttamente");
        return dipendenteModified;
    }

    public void findDipendenteByIdAndDelete(long dipendenteId) {
        Dipendente found= findDipendenteById(dipendenteId);
        this.dipendenteRepository.delete(found);
    }

    public String uploadAvatar(long dipendenteId, MultipartFile file) {
        Dipendente found= findDipendenteById(dipendenteId);
        try {
            Map result = getImageUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageURL = (String) result.get("url");

            found.setAvatarUrl(imageURL);
            this.dipendenteRepository.save(found);

            return imageURL;
        } catch (Exception e) {
            throw new BadRequestException("Ci sono stati problemi nel salvataggio del file!");
        }
    }
}
