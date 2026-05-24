package map.teledon.network.rpcprotocol;
import map.teledon.domain.CazCaritabil;

public class AdaugaDonatieRequest implements Request {
    private String numeDonator;
    private String adresa;
    private String telefon;
    private CazCaritabil caz;
    private double suma;

    public AdaugaDonatieRequest(String numeDonator, String adresa, String telefon, CazCaritabil caz, double suma) {
        this.numeDonator = numeDonator;
        this.adresa = adresa;
        this.telefon = telefon;
        this.caz = caz;
        this.suma = suma;
    }

    public String getNumeDonator() { return numeDonator; }
    public String getAdresa() { return adresa; }
    public String getTelefon() { return telefon; }
    public CazCaritabil getCaz() { return caz; }
    public double getSuma() { return suma; }
}