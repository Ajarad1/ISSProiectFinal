package map.teledon.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "Donatii")
public class Donatie extends Entity<Integer> implements Serializable {

    // 2. Mapăm relația către Donator (cheia străină idDonator din DB)
    @ManyToOne
    @JoinColumn(name = "idDonator")
    private Donator donator;

    // 3. Mapăm relația către CazCaritabil (cheia străină idCaz din DB)
    @ManyToOne
    @JoinColumn(name = "idCaz")
    private CazCaritabil caz;

    @Column(name = "sumaDonata")
    private Double sumaDonata;

    // Constructorul GOL obligatoriu pentru Hibernate
    public Donatie() {
    }

    // Constructorul tău original
    public Donatie(Donator donator, CazCaritabil caz, Double sumaDonata) {
        this.donator = donator;
        this.caz = caz;
        this.sumaDonata = sumaDonata;
    }

    public Donator getDonator() {
        return donator;
    }

    public void setDonator(Donator donator) {
        this.donator = donator;
    }

    public CazCaritabil getCaz() {
        return caz;
    }

    public void setCaz(CazCaritabil caz) {
        this.caz = caz;
    }

    public Double getSumaDonata() {
        return sumaDonata;
    }

    public void setSumaDonata(Double sumaDonata) {
        this.sumaDonata = sumaDonata;
    }
}