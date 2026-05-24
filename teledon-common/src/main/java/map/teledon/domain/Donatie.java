package map.teledon.domain;

import java.io.Serializable;

public class Donatie extends Entity<Integer> implements Serializable {
    private Donator donator;
    private CazCaritabil caz;
    private Double sumaDonata;

    public Donatie (Donator donator, CazCaritabil caz, Double sumaDonata) {
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
