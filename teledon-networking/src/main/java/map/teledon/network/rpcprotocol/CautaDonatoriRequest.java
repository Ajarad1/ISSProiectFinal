package map.teledon.network.rpcprotocol;

public class CautaDonatoriRequest implements Request {
    private String nume;

    public CautaDonatoriRequest(String nume) {
        this.nume = nume;
    }

    public String getNume() { return nume; }
}