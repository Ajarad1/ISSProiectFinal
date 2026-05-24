package map.teledon.network.rpcprotocol;

public class LoginRequest implements Request {
    private String username;
    private String parola;

    public LoginRequest(String username, String parola) {
        this.username = username;
        this.parola = parola;
    }

    public String getUsername() { return username; }
    public String getParola() { return parola; }
}