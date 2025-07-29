package alessiopanconi.u5w3d1.services;

import alessiopanconi.u5w3d1.DTO.DipendenteLoginDTO;
import alessiopanconi.u5w3d1.entities.Dipendente;
import alessiopanconi.u5w3d1.exceptions.UnauthorizedException;
import alessiopanconi.u5w3d1.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutenticazioneService {

        @Autowired
        private  DipendentiService dipendentiService;

        @Autowired
        private JWTTools jwtTools;

        public String checkAccessAndGenerateToken(DipendenteLoginDTO body)
        {
            Dipendente found = this.dipendentiService.findByEmail(body.email());
            if (found.getPassword().equals(body.password())) {
                String accessToken = jwtTools.createToken(found);
                return accessToken;
            } else {
                throw new UnauthorizedException("Password errata");
            }
        }
}
