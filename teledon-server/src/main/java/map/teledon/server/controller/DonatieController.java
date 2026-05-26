package map.teledon.server.controller;

import map.teledon.domain.CazCaritabil;
import map.teledon.domain.Donatie;
import map.teledon.domain.Donator;
import map.teledon.server.repository.CazCaritabilRepository;
import map.teledon.server.repository.DonatieRepository;
import map.teledon.server.repository.DonatorRepository;
import map.teledon.server.websocket.NotificationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/teledon/donatii")
public class DonatieController {

    @Autowired
    private DonatieRepository donatieRepository;

    @Autowired
    private DonatorRepository donatorRepository;

    @Autowired
    private CazCaritabilRepository cazCaritabilRepository;

    @PostMapping
    public ResponseEntity<?> addDonatie(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Extragem datele din JSON-ul primit de la frontend
            Integer idDonator = (Integer) payload.get("idDonator");
            Integer idCaz = (Integer) payload.get("idCaz");
            Double suma = Double.valueOf(payload.get("sumaDonata").toString());

            // 2. Căutăm entitățile în baza de date
            Donator donator = donatorRepository.findOne(idDonator);
            CazCaritabil caz = cazCaritabilRepository.findOne(idCaz);

            if (donator == null || caz == null) {
                return new ResponseEntity<>("Donatorul sau Cazul nu există!", HttpStatus.NOT_FOUND);
            }

            // 3. Creăm și salvăm donația
            Donatie donatie = new Donatie(donator, caz, suma);
            donatieRepository.save(donatie);

            // 4. Actualizăm suma totală a cazului caritabil (Business Logic)
            caz.setSumaTotala(caz.getSumaTotala() + suma);
            cazCaritabilRepository.update(caz);

            // 5. Trimitem notificare în timp real pe WebSocket
            NotificationWebSocketHandler.broadcast("S-au donat " + suma + " lei pentru: " + caz.getNumeCaz());

            return new ResponseEntity<>(donatie, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}