package map.teledon.network.rpcprotocol;
import map.teledon.domain.Donator;

public class CautaDonatoriResponse implements Response {
    private Iterable<Donator> donatori;

    public CautaDonatoriResponse(Iterable<Donator> donatori) {
        this.donatori = donatori;
    }

    public Iterable<Donator> getDonatori() { return donatori; }
}