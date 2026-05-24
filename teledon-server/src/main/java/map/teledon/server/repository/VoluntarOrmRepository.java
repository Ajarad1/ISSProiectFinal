package map.teledon.server.repository;

import map.teledon.domain.Voluntar;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class VoluntarOrmRepository implements VoluntarRepository {

    private static SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(VoluntarOrmRepository.class);

    public VoluntarOrmRepository() {
        logger.info("Initializare VoluntarOrmRepository (Hibernate)");
        if (sessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // citeste hibernate.cfg.xml unde ai setat afisarea SQL [cite: 11]
                    .build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                logger.error("Eroare la initializarea SessionFactory: " + e.getMessage());
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
    }

    @Override
    public Voluntar findByUsername(String username) {
        logger.traceEntry("Cautare voluntar dupa username {}", username);
        try (Session session = sessionFactory.openSession()) {
            Voluntar v = session.createQuery("from Voluntar where username = :u", Voluntar.class)
                    .setParameter("u", username)
                    .uniqueResult();
            logger.traceExit(v);
            return v;
        }
    }

    @Override
    public Voluntar findOne(Integer id) {
        logger.traceEntry("Cautare voluntar dupa id {}", id);
        try (Session session = sessionFactory.openSession()) {
            Voluntar v = session.get(Voluntar.class, id);
            logger.traceExit(v);
            return v;
        }
    }

    @Override
    public Iterable<Voluntar> findAll() {
        logger.traceEntry("Afisare toti voluntarii");
        try (Session session = sessionFactory.openSession()) {
            List<Voluntar> voluntari = session.createQuery("from Voluntar", Voluntar.class).list();
            logger.traceExit(voluntari);
            return voluntari;
        }
    }

    @Override
    public Voluntar save(Voluntar entity) {
        logger.traceEntry("Salvare voluntar {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
                logger.trace("Voluntar salvat cu succes");
                return entity;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                logger.error(ex);
                throw ex;
            }
        }
    }

    @Override
    public Voluntar delete(Integer id) {
        logger.traceEntry("Sterge voluntarul cu id {}", id);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Voluntar v = session.get(Voluntar.class, id);
                if (v != null) {
                    session.remove(v);
                }
                tx.commit();
                logger.traceExit(v);
                return v;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                logger.error(ex);
                throw ex;
            }
        }
    }

    @Override
    public Voluntar update(Voluntar entity) {
        logger.traceEntry("Modifica voluntar {}", entity);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Voluntar updated = (Voluntar) session.merge(entity);
                tx.commit();
                logger.traceExit(updated);
                return updated;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                logger.error(ex);
                throw ex;
            }
        }
    }
}