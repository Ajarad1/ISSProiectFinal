package map.teledon.services;

import map.teledon.domain.CazCaritabil;
import java.io.Serializable;

public interface TeledonObserver extends Serializable {
    // Metoda apelată de server pentru a anunța clienții că un caz a fost actualizat
    void cazActualizat(CazCaritabil caz) throws Exception;
}