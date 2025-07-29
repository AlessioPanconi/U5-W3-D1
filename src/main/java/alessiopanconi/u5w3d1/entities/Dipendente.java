package alessiopanconi.u5w3d1.entities;

import alessiopanconi.u5w3d1.enums.Ruolo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "dipendente")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"password","authorities","enabled","accountNonExpired","credentialsNonExpired","accountNonLocked"})
public class Dipendente implements UserDetails {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long idDipendente;
    private String username;
    private String nome;
    private String cognome;
    private String email;
    @Column(name = "avatar_url")
    private String avatarUrl;
    private String password;
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    public Dipendente(String username, String nome, String cognome, String email, String avatarUrl,String password) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.password = password;
        this.ruolo = Ruolo.UTENTE;
    }

    @Override
    public String toString() {
        return "Dipendente{" +
                "idPrenotazione=" + idDipendente +
                ", username='" + username + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }
}
