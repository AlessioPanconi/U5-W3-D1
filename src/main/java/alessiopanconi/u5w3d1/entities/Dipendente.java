package alessiopanconi.u5w3d1.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dipendente")
@Getter
@Setter
@NoArgsConstructor
public class Dipendente {

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

    public Dipendente(String username, String nome, String cognome, String email, String avatarUrl,String password) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.password = password;
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
}
