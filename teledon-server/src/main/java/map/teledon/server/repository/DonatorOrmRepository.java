package map.teledon.server.repository;

import map.teledon.domain.Donator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DonatorOrmRepository implements DonatorRepository {

    @Autowired
    private static SessionFactory sessionFactory;

    public DonatorOrmRepository() {
        if (sessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // Citește automat din hibernate.cfg.xml
                    .build();
            try {
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
                e.printStackTrace(); // E bine să lăsăm asta pentru a vedea eventualele erori în consolă
            }
        }
    }

    @Override
    public Donator save(Donator entity) {
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
    public Donator delete(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Donator donator = session.get(Donator.class, id);
                if (donator != null) {
                    session.remove(donator);
                }
                tx.commit();
                return donator;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Donator update(Donator entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Donator donator = session.merge(entity);
                tx.commit();
                return donator;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Donator findOne(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Donator.class, id);
        }
    }

    @Override
    public Iterable<Donator> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Donator", Donator.class).list();
        }
    }
    @Override
    public Iterable<Donator> filterByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            // Căutăm donatorii al căror nume conține textul introdus
            return session.createQuery("from Donator where nume like :nume", Donator.class)
                    .setParameter("nume", "%" + name + "%")
                    .list();
        }
    }



    public Donator findByTelefon(String telefon) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Donator where numarTelefon = :telefon", Donator.class)
                    .setParameter("telefon", telefon)
                    .uniqueResult();
        }
    }
}