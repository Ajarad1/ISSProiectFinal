package map.teledon.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import map.teledon.domain.CazCaritabil;
import map.teledon.domain.Donator;
import map.teledon.domain.Voluntar;
import map.teledon.services.TeledonObserver;
import map.teledon.services.TeledonServices;

public class MainController implements TeledonObserver {
    @FXML private TableView<CazCaritabil> tabelCazuri;
    @FXML private TableColumn<CazCaritabil, String> colNumeCaz;
    @FXML private TableColumn<CazCaritabil, Double> colSuma;

    @FXML private TextField searchField;
    @FXML private ListView<Donator> listaDonatori;

    @FXML private TextField numeField;
    @FXML private TextField adresaField;
    @FXML private TextField telefonField;
    @FXML private TextField sumaField;

    private TeledonServices service;
    private Voluntar voluntarLogat;
    private ObservableList<CazCaritabil> modelCazuri = FXCollections.observableArrayList();

    public void setService(TeledonServices service, Voluntar voluntar) {
        this.service = service;
        this.voluntarLogat = voluntar;
        initData();
    }

    public void setServer(TeledonServices service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        colNumeCaz.setCellValueFactory(new PropertyValueFactory<>("numeCaz"));
        colSuma.setCellValueFactory(new PropertyValueFactory<>("sumaTotala"));
        tabelCazuri.setItems(modelCazuri);

        listaDonatori.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                numeField.setText(newV.getNume());
                adresaField.setText(newV.getAdresa());
                telefonField.setText(newV.getNumarTelefon());
            }
        });
    }

    public void initData() {
        modelCazuri.clear();
        try {
            for (CazCaritabil caz : service.getAllCazuri()) {
                System.out.println("Am primit de la server cazul: " + caz.getId()); // <-- ADAUGA ASTA
                modelCazuri.add(caz);
            }
        } catch (Exception e) {
            System.err.println("Eroare la aducerea cazurilor de pe server: " + e.getMessage());
        }
    }

    @FXML
    public void handleSearch() {
        String nume = searchField.getText();
        ObservableList<Donator> results = FXCollections.observableArrayList();

        try {
            for (Donator d : service.cautaDonatori(nume)) {
                results.add(d);
            }
            listaDonatori.setItems(results);
        } catch (Exception e) {
            System.err.println("Eroare la cautarea donatorului pe server: " + e.getMessage());
        }
    }

    @FXML
    public void handleDonate() {
        try {
            CazCaritabil cazSelectat = tabelCazuri.getSelectionModel().getSelectedItem();
            String nume = numeField.getText();
            String adresa = adresaField.getText();
            String telefon = telefonField.getText();
            double suma = Double.parseDouble(sumaField.getText());

            service.adaugaDonatie(nume, adresa, telefon, cazSelectat, suma);

            new Alert(Alert.AlertType.INFORMATION, "Donatia a fost inregistrata cu succes!").show();

            numeField.clear(); adresaField.clear(); telefonField.clear(); sumaField.clear();
            initData();

        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Suma trebuie sa fie un numar valid!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void handleUpdate() {
        Donator donatorSelectat = listaDonatori.getSelectionModel().getSelectedItem();
        if (donatorSelectat == null) {
            new Alert(Alert.AlertType.WARNING, "Selectati un donator din lista de cautare pentru a-l modifica!").show();
            return;
        }
        try {
            String adresaNoua = adresaField.getText();
            String telefonNou = telefonField.getText();

            service.modificaDonator(donatorSelectat, adresaNoua, telefonNou);

            new Alert(Alert.AlertType.INFORMATION, "Datele donatorului au fost actualizate!").show();
            handleSearch(); // Dam refresh vizual la lista
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    @FXML
    public void handleLogout() {
        try {
            service.logout(voluntarLogat, this);

            Stage stage = (Stage) numeField.getScene().getWindow();
            stage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 250);

            LoginController loginCtrl = fxmlLoader.getController();
            loginCtrl.setService(service);

            Stage loginStage = new Stage();
            loginStage.setTitle("Teledon - Autentificare");
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void cazActualizat(CazCaritabil caz) throws Exception {
        Platform.runLater(() -> { initData(); });
    }
    public void setVoluntarLogat(Voluntar voluntar) {
        this.voluntarLogat = voluntar;
    }
}