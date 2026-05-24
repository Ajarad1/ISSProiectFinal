package map.teledon.server;

import map.teledon.domain.CazCaritabil;
import map.teledon.server.repository.CazCaritabilOrmRepository;

public class TestHibernate {
    public static void main(String[] args) {
        CazCaritabilOrmRepository repo = new CazCaritabilOrmRepository();


        System.out.println("--- Test adaugare caz nou ---");
        CazCaritabil cazNou = new CazCaritabil("Caz Test Hibernate", 100.0);
        repo.save(cazNou);



        System.out.println("--- Test modificare suma ---");
        CazCaritabil deModificat = repo.findOne(cazNou.getId());
        if (deModificat != null) {
            deModificat.setSumaTotala(500.0);
            repo.update(deModificat);
        }
    }
}