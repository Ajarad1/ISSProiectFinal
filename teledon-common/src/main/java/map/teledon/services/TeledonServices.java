package map.teledon.services;

import map.teledon.domain.CazCaritabil;
import map.teledon.domain.Donator;
import map.teledon.domain.Voluntar;

public interface TeledonServices {
    // La login, clientul trimite și observer-ul (referința către el însuși)
    // pentru ca serverul să știe pe cine să notifice mai târziu
    Voluntar login(String username, String parola, TeledonObserver client) throws Exception;

    void logout(Voluntar voluntar, TeledonObserver client) throws Exception;

    Iterable<CazCaritabil> getAllCazuri() throws Exception;

    Iterable<Donator> cautaDonatori(String nume) throws Exception;

    void adaugaDonatie(String numeDonator, String adresa, String telefon, CazCaritabil caz, double suma) throws Exception;

    void modificaDonator(Donator donator, String adresaNoua, String telefonNou) throws Exception;
}