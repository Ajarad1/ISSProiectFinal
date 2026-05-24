package map.teledon.server;

import map.teledon.domain.*;
import map.teledon.server.repository.*;
import map.teledon.services.TeledonObserver;
import map.teledon.services.TeledonServices;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeledonServicesImpl implements TeledonServices {

    // Repository-urile tale (fara I in fata, exact cum le ai tu)
    private VoluntarRepository voluntarRepo;
    private CazCaritabilRepository cazRepo;
    private DonatorRepository donatorRepo;
    private DonatieRepository donatieRepo;

    // Aici tinem minte clientii logati la un moment dat (Username -> Observer)
    private Map<String, TeledonObserver> loggedClients;

    public TeledonServicesImpl(VoluntarRepository voluntarRepo, CazCaritabilRepository cazRepo,
                               DonatorRepository donatorRepo, DonatieRepository donatieRepo) {
        this.voluntarRepo = voluntarRepo;
        this.cazRepo = cazRepo;
        this.donatorRepo = donatorRepo;
        this.donatieRepo = donatieRepo;
        // Folosim ConcurrentHashMap pentru ca e thread-safe (mai multi clienti se pot loga simultan)
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Voluntar login(String username, String parola, TeledonObserver client) throws Exception {
        Voluntar voluntar = voluntarRepo.findByUsername(username);
        if (voluntar != null && voluntar.getPassword().equals(parola)) {
            if (loggedClients.get(username) != null) {
                throw new Exception("Voluntarul este deja logat!");
            }
            loggedClients.put(username, client); // Salvam clientul curent
            return voluntar;
        }
        throw new Exception("Autentificare esuata!");
    }

    @Override
    public synchronized void logout(Voluntar voluntar, TeledonObserver client) throws Exception {
        TeledonObserver localClient = loggedClients.remove(voluntar.getUsername());
        if (localClient == null) {
            throw new Exception("Voluntarul nu este logat.");
        }
    }

    @Override
    public Iterable<CazCaritabil> getAllCazuri() throws Exception {
        return cazRepo.findAll();
    }

    @Override
    public Iterable<Donator> cautaDonatori(String nume) throws Exception {
        return donatorRepo.filterByName(nume);
    }

    @Override
    public void adaugaDonatie(String numeDonator, String adresa, String telefon, CazCaritabil caz, double suma) throws Exception {
        // 1. Cautam / cream donatorul
        Donator donator = null;
        for (Donator d : donatorRepo.filterByName(numeDonator)) {
            if (d.getNume().equals(numeDonator)) {
                donator = d;
                break;
            }
        }
        if (donator == null) {
            donator = new Donator(numeDonator, adresa, telefon);
            donator = donatorRepo.save(donator);
        }

        // 2. Salvam donatia
        donatieRepo.save(new Donatie(donator, caz, suma));

        // 3. Actualizam suma la caz
        caz.setSumaTotala(caz.getSumaTotala() + suma);
        cazRepo.update(caz);

        // 4. NOU: NOTIFICAM TOTI CLIENTII LOGATI CA S-A SCHIMBAT SUMA!
        notifyClientsCazActualizat(caz);
    }

    @Override
    public void modificaDonator(Donator donator, String adresaNoua, String telefonNou) throws Exception {
        donator.setAdresa(adresaNoua);
        donator.setNumarTelefon(telefonNou);
        donatorRepo.update(donator);
    }

    // --- Metoda privata care cheama update-ul pe interfata fiecarui client ---
    private void notifyClientsCazActualizat(CazCaritabil caz) {
        // Folosim un ExecutorService pentru a trimite notificarile pe alte thread-uri.
        // Asa nu blocam serverul daca un client are internetul mai incet.
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (TeledonObserver client : loggedClients.values()) {
            executor.execute(() -> {
                try {
                    client.cazActualizat(caz);
                } catch (Exception e) {
                    System.err.println("Eroare la notificarea clientului.");
                }
            });
        }
        executor.shutdown();
    }
}