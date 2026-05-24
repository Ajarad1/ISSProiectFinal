package map.teledon.server.controller;

import map.teledon.domain.CazCaritabil;
import map.teledon.server.repository.CazCaritabilRepository;
import map.teledon.server.websocket.NotificationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/teledon/cazuri")
public class CazCaritabilController {

    @Autowired
    private CazCaritabilRepository repository;

    // 1. Afisarea tuturor + 2. Filtrarea (folosind un parametru opțional)
    @GetMapping
    public List<CazCaritabil> getAll(@RequestParam(value = "numeCaz", required = false) String numeCaz) { // <-- MODIFICAREA AICI
        Iterable<CazCaritabil> cazuri = repository.findAll();
        List<CazCaritabil> rezultate = new ArrayList<>();

        cazuri.forEach(caz -> {
            if (numeCaz == null || caz.getNumeCaz().toLowerCase().contains(numeCaz.toLowerCase())) {
                rezultate.add(caz);
            }
        });
        return rezultate;
    }

    // 3. Cautare dupa ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) { // <-- MODIFICAREA AICI
        CazCaritabil caz = repository.findOne(id);
        if (caz == null) {
            return new ResponseEntity<>("Cazul caritabil nu a fost găsit", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(caz, HttpStatus.OK);
    }

    // 4. Creare
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CazCaritabil caz) {
        try {
            CazCaritabil savedCaz = repository.save(caz);
            NotificationWebSocketHandler.broadcast("ADAUGARE: " + savedCaz.getNumeCaz());
            return new ResponseEntity<>(savedCaz, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 5. Modificare
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody CazCaritabil caz) { // <-- MODIFICAREA AICI
        try {
            caz.setId(id);
            CazCaritabil updatedCaz = repository.update(caz);
            NotificationWebSocketHandler.broadcast("MODIFICARE: " + updatedCaz.getNumeCaz());
            return new ResponseEntity<>(updatedCaz, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 6. Stergere
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) { // <-- MODIFICAREA AICI
        try {
            CazCaritabil deletedCaz = repository.delete(id);
            if (deletedCaz == null) {
                return new ResponseEntity<>("Cazul nu exista", HttpStatus.NOT_FOUND);
            }
            NotificationWebSocketHandler.broadcast("STERGERE: ID " + id);
            return new ResponseEntity<>(deletedCaz, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}