package map.teledon.server.repository;

import map.teledon.domain.Donatie;
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
public class DonatieOrmRepository implements DonatieRepository {

    @Autowired
    private static SessionFactory sessionFactory;

    public DonatieOrmRepository(){
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
    public Donatie save(Donatie entity) {
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
    public Donatie delete(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Donatie donatie = session.get(Donatie.class, id);
                if (donatie != null) {
                    session.remove(donatie);
                }
                tx.commit();
                return donatie;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Donatie update(Donatie entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Donatie donatie = session.merge(entity);
                tx.commit();
                return donatie;
            } catch (RuntimeException ex) {
                if (tx != null) tx.rollback();
                throw ex;
            }
        }
    }

    @Override
    public Donatie findOne(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Donatie.class, id);
        }
    }

    @Override
    public Iterable<Donatie> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Donatie", Donatie.class).list();
        }
    }
}