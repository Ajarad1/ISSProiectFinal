package map.teledon.network.rpcprotocol;
import map.teledon.domain.CazCaritabil;

public class GetCazuriResponse implements Response {
    private Iterable<CazCaritabil> cazuri;

    public GetCazuriResponse(Iterable<CazCaritabil> cazuri) {
        this.cazuri = cazuri;
    }

    public Iterable<CazCaritabil> getCazuri() { return cazuri; }
}