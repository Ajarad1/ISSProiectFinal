package map.teledon.server.controller;

import map.teledon.domain.Donator;
import map.teledon.server.repository.DonatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/teledon/donatori")
public class DonatorController {

    @Autowired
    private DonatorRepository donatorRepository;

    // Căutare donator existent după numărul de telefon
    @GetMapping("/cauta")
    public ResponseEntity<?> getByTelefon(@RequestParam("telefon") String telefon) {
        try {
            Donator donator = donatorRepository.findByTelefon(telefon);
            if (donator == null) {
                return new ResponseEntity<>("Donatorul nu a fost găsit.", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(donator, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Adăugare donator nou
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Donator donator) {
        try {
            Donator savedDonator = donatorRepository.save(donator);
            return new ResponseEntity<>(savedDonator, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}