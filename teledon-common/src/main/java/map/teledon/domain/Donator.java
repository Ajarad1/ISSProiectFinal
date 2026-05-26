package map.teledon.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Donatori")
public class Donator extends Entity<Integer> implements Serializable {

    @Column(name = "nume")
    private String nume;

    @Column(name = "adresa")
    private String adresa;

    @Column(name = "numarTelefon")
    private String numarTelefon;

    // 1. Constructorul GOL este OBLIGATORIU pentru Hibernate
    public Donator() {
    }

    // Constructorul tău original
    public Donator(String nume, String adresa, String numarTelefon) {
        this.nume = nume;
        this.adresa = adresa;
        this.numarTelefon = numarTelefon;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNumarTelefon() {
        return numarTelefon;
    }

    public void setNumarTelefon(String numarTelefon) {
        this.numarTelefon = numarTelefon;
    }

    @Override
    public String toString() {
        return this.getNume() + " (" + this.getNumarTelefon() + ")";
    }
}