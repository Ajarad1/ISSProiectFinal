package map.teledon.client; // Modifică pachetul dacă este nevoie

import map.teledon.domain.CazCaritabil;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class TeledonRestClient {

    public static void main(String[] args) {
        // 1. Configurarea RestClient cu interceptorul cerut
        RestClient client = RestClient.builder()
                .baseUrl("http://localhost:8080/teledon/cazuri")
                .requestInterceptor(new LoggingInterceptor())
                .build();

        try {
            System.out.println("\n===== 1. TEST GET ALL =====");
            CazCaritabil[] toateCazurile = client.get()
                    .retrieve()
                    .body(CazCaritabil[].class);
            if (toateCazurile != null) {
                for (CazCaritabil c : toateCazurile) {
                    System.out.println("Am găsit cazul: " + c.getId() + " - " + c.getNumeCaz());
                }
            }

            System.out.println("\n===== 2. TEST POST (Creare) =====");
            CazCaritabil cazNou = new CazCaritabil("Caz Test din Java Client", 500.0);
            CazCaritabil cazSalvat = client.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cazNou)
                    .retrieve()
                    .body(CazCaritabil.class);
            System.out.println("Caz creat cu succes! ID-ul primit de la server este: " + cazSalvat.getId());

            System.out.println("\n===== 3. TEST GET BY ID =====");
            CazCaritabil cazGasit = client.get()
                    .uri("/{id}", cazSalvat.getId())
                    .retrieve()
                    .body(CazCaritabil.class);
            System.out.println("Verificare căutare după ID: " + cazGasit.getNumeCaz());

            System.out.println("\n===== 4. TEST PUT (Modificare) =====");
            cazGasit.setSumaTotala(2500.0);
            CazCaritabil cazModificat = client.put()
                    .uri("/{id}", cazGasit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cazGasit)
                    .retrieve()
                    .body(CazCaritabil.class);
            System.out.println("Cazul a fost modificat. Noua sumă este: " + cazModificat.getSumaTotala());

            System.out.println("\n===== 5. TEST DELETE (Ștergere) =====");
            CazCaritabil cazSters = client.delete()
                    .uri("/{id}", cazGasit.getId())
                    .retrieve()
                    .body(CazCaritabil.class);
            System.out.println("Cazul cu ID " + cazSters.getId() + " a fost șters din baza de date.");

        } catch (Exception e) {
            System.out.println("A apărut o eroare în timpul apelurilor REST: " + e.getMessage());
        }
    }
}