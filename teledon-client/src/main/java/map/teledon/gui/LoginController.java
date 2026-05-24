package map.teledon.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import map.teledon.domain.Voluntar;
import map.teledon.services.TeledonServices;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private TeledonServices service;

    public void setService(TeledonServices service) {
        this.service = service;
    }
    public void setServer(TeledonServices server) {
        this.service = server;
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String parola = passwordField.getText();

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
            Parent root = loader.load();


            MainController mainCtrl = loader.getController();
            mainCtrl.setServer(service);


            Voluntar crtVoluntar = service.login(username, parola, mainCtrl);
            mainCtrl.setVoluntarLogat(crtVoluntar);
            mainCtrl.initData();

            Stage stage = new Stage();
            stage.setTitle("Teledon - " + crtVoluntar.getUsername());
            stage.setScene(new Scene(root));
            stage.show();


            ((Stage) usernameField.getScene().getWindow()).close();

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void deschideFereastraPrincipala(Voluntar voluntar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
            Scene scene = new Scene(loader.load());

            MainController mainController = loader.getController();
            mainController.setService(service, voluntar); // dam service-ul mai departe

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Teledon - Autentificat ca: " + voluntar.getUsername());
            currentStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}