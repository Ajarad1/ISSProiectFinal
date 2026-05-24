package map.teledon.network.rpcprotocol;
import map.teledon.domain.Donator;

public class ModificaDonatorRequest implements Request {
    private Donator donator;
    private String adresaNoua;
    private String telefonNou;

    public ModificaDonatorRequest(Donator donator, String adresaNoua, String telefonNou) {
        this.donator = donator;
        this.adresaNoua = adresaNoua;
        this.telefonNou = telefonNou;
    }

    public Donator getDonator() { return donator; }
    public String getAdresaNoua() { return adresaNoua; }
    public String getTelefonNou() { return telefonNou; }
}