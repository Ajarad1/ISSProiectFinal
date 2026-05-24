package map.teledon.network.rpcprotocol;
import map.teledon.domain.Voluntar;

public class OkResponse implements Response {
    private Voluntar voluntar; // Folosit uneori la login pentru a returna voluntarul gasit

    public OkResponse() {}
    public OkResponse(Voluntar voluntar) { this.voluntar = voluntar; }
    public Voluntar getVoluntar() { return voluntar; }
}