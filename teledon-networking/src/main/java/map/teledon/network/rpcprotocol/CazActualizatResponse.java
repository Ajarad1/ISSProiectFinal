package map.teledon.network.rpcprotocol;
import map.teledon.domain.CazCaritabil;

public class CazActualizatResponse implements UpdateResponse {
    private CazCaritabil caz;

    public CazActualizatResponse(CazCaritabil caz) {
        this.caz = caz;
    }

    public CazCaritabil getCaz() { return caz; }
}