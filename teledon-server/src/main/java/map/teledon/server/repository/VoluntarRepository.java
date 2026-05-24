package map.teledon.server.repository;

import map.teledon.domain.Voluntar;

public interface VoluntarRepository extends Repository<Integer, Voluntar>{
    Voluntar findByUsername(String username);
}
