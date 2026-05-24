package map.teledon.domain;

import java.io.Serializable;

public class Donator extends Entity<Integer> implements Serializable {
    private String nume;
    private String adresa;
    private String numarTelefon;

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

