package map.teledon.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import map.teledon.gui.LoginController;
import map.teledon.network.rpcprotocol.TeledonServerRpcProxy;
import map.teledon.services.TeledonServices;
// Aici importi si LoginController-ul tau

public class StartClient extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In the start...");

        // 1. Ne conectam la Server prin Proxy!
        TeledonServices server = new TeledonServerRpcProxy(defaultServer, defaultChatPort);

        // 2. Incarcam fereastra de Login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
        Scene scene = new Scene(loader.load());

        // 3. Facem legatura: ii dam Controllerului de Login referinta catre "server" (care e de fapt proxy-ul)
        LoginController ctrl = loader.getController();
        ctrl.setServer(server); // Trebuie sa ai aceasta metoda in LoginController

        primaryStage.setTitle("Teledon - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}