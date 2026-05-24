package map.teledon.network.rpcprotocol;

import map.teledon.domain.*;
import map.teledon.services.TeledonObserver;
import map.teledon.services.TeledonServices;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TeledonServerRpcProxy implements TeledonServices {
    private String host;
    private int port;

    private TeledonObserver client; // Controller-ul tau grafic

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    // Aici se pun la coada raspunsurile normale primite de la server
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public TeledonServerRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<>();
    }

    private void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader(); // Pormin thread-ul care asculta non-stop
        } catch (Exception e) {
            throw new Exception("Eroare la conectarea cu serverul: " + e.getMessage());
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
            client = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception e) {
            throw new Exception("Eroare la trimiterea cererii: " + e.getMessage());
        }
    }

    private Response readResponse() throws Exception {
        Response response = null;
        try {
            response = qresponses.take(); // Asteapta pana apare un raspuns in coada
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Voluntar login(String username, String parola, TeledonObserver client) throws Exception {
        initializeConnection(); // Se conecteaza cand da login
        sendRequest(new LoginRequest(username, parola));
        Response response = readResponse();

        if (response instanceof OkResponse) {
            this.client = client; // Tinem minte cine e clientul pentru a-i trimite notificari mai tarziu
            return ((OkResponse) response).getVoluntar();
        }
        if (response instanceof ErrorResponse) {
            closeConnection();
            throw new Exception(((ErrorResponse) response).getMessage());
        }
        return null;
    }

    @Override
    public void logout(Voluntar voluntar, TeledonObserver client) throws Exception {
        sendRequest(new LogoutRequest(voluntar));
        Response response = readResponse();
        closeConnection(); // La logout taiem cablul
        if (response instanceof ErrorResponse) {
            throw new Exception(((ErrorResponse) response).getMessage());
        }
    }

    @Override
    public Iterable<CazCaritabil> getAllCazuri() throws Exception {
        sendRequest(new GetCazuriRequest());
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            throw new Exception(((ErrorResponse) response).getMessage());
        }
        return ((GetCazuriResponse) response).getCazuri();
    }

    @Override
    public Iterable<Donator> cautaDonatori(String nume) throws Exception {
        sendRequest(new CautaDonatoriRequest(nume));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            throw new Exception(((ErrorResponse) response).getMessage());
        }
        return ((CautaDonatoriResponse) response).getDonatori();
    }

    @Override
    public void adaugaDonatie(String numeDonator, String adresa, String telefon, CazCaritabil caz, double suma) throws Exception {
        sendRequest(new AdaugaDonatieRequest(numeDonator, adresa, telefon, caz, suma));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            throw new Exception(((ErrorResponse) response).getMessage());
        }
    }

    @Override
    public void modificaDonator(Donator donator, String adresaNoua, String telefonNou) throws Exception {
        sendRequest(new ModificaDonatorRequest(donator, adresaNoua, telefonNou));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            throw new Exception(((ErrorResponse) response).getMessage());
        }
    }

    // Aceasta metoda este apelata doar de ReaderThread cand serverul trimite ceva nesolicitat (notificare)
    private void handleUpdate(UpdateResponse update) {
        if (update instanceof CazActualizatResponse) {
            CazActualizatResponse cazRes = (CazActualizatResponse) update;
            try {
                // Instiintam controller-ul grafic ca s-a modificat ceva
                client.cazActualizat(cazRes.getCaz());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    // Un fir de executie separat care asculta la nesfarsit in mufa de retea
    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Raspuns primit de la server: " + response);

                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Eroare de citire de la server: " + e);
                    finished = true;
                }
            }
        }
    }
}