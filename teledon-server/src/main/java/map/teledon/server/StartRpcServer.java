package map.teledon.server;

import map.teledon.network.rpcprotocol.TeledonClientRpcWorker;
import map.teledon.server.repository.*;
import map.teledon.services.TeledonServices;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/bd.properties"));
        } catch (Exception e) {
            System.err.println("Nu s-a gasit bd.config! " + e);
            return;
        }

        // 2. Initializam baza de date (aici pui numele exacte ale claselor tale din repo)
        //VoluntarRepository voluntarRepo = new VoluntarDbRepo(serverProps);
        //CazCaritabilRepository cazRepo = new CazCaritabilDbRepo(serverProps);
        VoluntarRepository voluntarRepo = new VoluntarOrmRepository();
        CazCaritabilRepository cazRepo = new CazCaritabilOrmRepository();
        DonatorRepository donatorRepo = new DonatorDbRepository(serverProps);
        DonatieRepository donatieRepo = new DonatieDbRepo(serverProps);

        // 3. Cream Inima Serverului
        TeledonServices serverImpl = new TeledonServicesImpl(voluntarRepo, cazRepo, donatorRepo, donatieRepo);

        // 4. Deschidem portul de retea si ascultam clienti
        try (ServerSocket serverSocket = new ServerSocket(defaultPort)) {
            System.out.println("Serverul a pornit si asteapta clienti pe portul " + defaultPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("S-a conectat un client nou!");

                // Cream un Worker dedicat pentru acest client si il rulam pe un fir separat (Thread)
                Thread t = new Thread(new TeledonClientRpcWorker(serverImpl, clientSocket));
                t.start();
            }
        } catch (Exception e) {
            System.err.println("Eroare la pornirea serverului: " + e.getMessage());
        }
    }
}