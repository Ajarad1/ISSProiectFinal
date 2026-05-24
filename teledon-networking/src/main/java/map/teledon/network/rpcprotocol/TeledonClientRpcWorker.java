package map.teledon.network.rpcprotocol;

import map.teledon.domain.CazCaritabil;
import map.teledon.domain.Donator;
import map.teledon.domain.Voluntar;
import map.teledon.services.TeledonObserver;
import map.teledon.services.TeledonServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TeledonClientRpcWorker implements Runnable, TeledonObserver {
    private TeledonServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public TeledonClientRpcWorker(TeledonServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (Exception e) {
                if (connected) {
                    System.out.println("Eroare la citirea din socket: " + e.getMessage());
                }
                break;
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request instanceof LoginRequest) {
            System.out.println("Login request...");
            LoginRequest logReq = (LoginRequest) request;
            try {
                // Aici apelam logica reala de pe server, dandu-i si referinta catre acest Worker (this)
                Voluntar voluntar = server.login(logReq.getUsername(), logReq.getParola(), this);
                return new OkResponse(voluntar);
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest) {
            System.out.println("Logout request...");
            LogoutRequest logReq = (LogoutRequest) request;
            try {
                server.logout(logReq.getVoluntar(), this);
                connected = false; // Oprim ascultarea
                return new OkResponse();
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof GetCazuriRequest) {
            System.out.println("GetCazuri request...");
            try {
                return new GetCazuriResponse(server.getAllCazuri());
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof CautaDonatoriRequest) {
            System.out.println("CautaDonatori request...");
            CautaDonatoriRequest req = (CautaDonatoriRequest) request;
            try {
                return new CautaDonatoriResponse(server.cautaDonatori(req.getNume()));
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof AdaugaDonatieRequest) {
            System.out.println("AdaugaDonatie request...");
            AdaugaDonatieRequest req = (AdaugaDonatieRequest) request;
            try {
                server.adaugaDonatie(req.getNumeDonator(), req.getAdresa(), req.getTelefon(), req.getCaz(), req.getSuma());
                return new OkResponse();
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof ModificaDonatorRequest) {
            System.out.println("ModificaDonator request...");
            ModificaDonatorRequest req = (ModificaDonatorRequest) request;
            try {
                server.modificaDonator(req.getDonator(), req.getAdresaNoua(), req.getTelefonNou());
                return new OkResponse();
            } catch (Exception e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response: " + response);
        output.writeObject(response);
        output.flush();
    }

    // --- Implementarea metodei din TeledonObserver ---
    // Cand se face o donatie, Serverul cheama aceasta metoda.
    // Iar Worker-ul ia cazul actualizat si il trimite prin retea clientului sau!
    @Override
    public void cazActualizat(CazCaritabil caz) throws Exception {
        System.out.println("Caz actualizat: trimitem notificarea catre client...");
        try {
            sendResponse(new CazActualizatResponse(caz));
        } catch (IOException e) {
            throw new Exception("Eroare la trimiterea notificarii: " + e.getMessage());
        }
    }
}