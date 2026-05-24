package map.teledon.server.repository;

import map.teledon.domain.CazCaritabil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;

@Component
public class CazCaritabilOrmRepository implements CazCaritabilRepository {

    private static SessionFactory sessionFactory;

    public CazCaritabilOrmRepository() {
        if (sessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
    }

    @Override
    public CazCaritabil findOne(Integer id) { // Returnează E
        try (Session session = sessionFactory.openSession()) {
            return session.get(CazCaritabil.class, id);
        }
    }

    @Override
    public Iterable<CazCaritabil> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from CazCaritabil", CazCaritabil.class).list();
        }
    }

    @Override
    public CazCaritabil save(CazCaritabil entity) { // Trebuie să returneze CazCaritabil
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(entity);
                tx.commit();
                return entity;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public CazCaritabil delete(Integer id) { // Trebuie să returneze CazCaritabil
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                CazCaritabil crit = session.get(CazCaritabil.class, id);
                if (crit != null) {
                    session.remove(crit);
                }
                tx.commit();
                return crit;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public CazCaritabil update(CazCaritabil entity) { // Trebuie să returneze CazCaritabil
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                CazCaritabil updated = (CazCaritabil) session.merge(entity);
                tx.commit();
                return updated;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                throw ex;
            }
        }
    }
}