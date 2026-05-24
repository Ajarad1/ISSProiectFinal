package map.teledon.network.rpcprotocol;
import map.teledon.domain.Voluntar;

public class LogoutRequest implements Request {
    private Voluntar voluntar;

    public LogoutRequest(Voluntar voluntar) {
        this.voluntar = voluntar;
    }

    public Voluntar getVoluntar() { return voluntar; }
}