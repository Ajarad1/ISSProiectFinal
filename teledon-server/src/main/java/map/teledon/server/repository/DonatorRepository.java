package map.teledon.server.repository;

import map.teledon.domain.Donator;

public interface DonatorRepository extends Repository<Integer, Donator>{
    Iterable<Donator> filterByName(String name);
    Donator findByTelefon(String telefon);
}
