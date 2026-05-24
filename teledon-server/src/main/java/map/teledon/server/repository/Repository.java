package map.teledon.server.repository;

import map.teledon.domain.Entity;

public interface Repository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity);
    E delete(ID id);
    E update(E entity);
}
