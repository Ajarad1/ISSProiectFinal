package map.teledon.domain;

import jakarta.persistence.*;

import java.io.Serializable;
@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "Voluntari")
public class Voluntar extends Entity<Integer> implements Serializable {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "parola")
    private String parola;

    public Voluntar() {}
    public Voluntar(String username, String password) {
        this.username = username;
        this.parola = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return parola;
    }
    public void setPassword(String password) {
        this.parola = password;
    }
}
