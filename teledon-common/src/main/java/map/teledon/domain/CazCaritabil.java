package map.teledon.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@jakarta.persistence.Entity
@Table(name = "CazuriCaritabile")
public class CazCaritabil extends Entity<Integer> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numeCaz")
    private String numeCaz;

    @Column(name = "sumaTotala")
    private Double sumaTotala;


    public CazCaritabil() {}

    public CazCaritabil(String numeCaz, Double sumaTotala) {
        this.numeCaz = numeCaz;
        this.sumaTotala = sumaTotala;
    }

    // Gettere și Settere obligatorii
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNumeCaz() { return numeCaz; }
    public void setNumeCaz(String numeCaz) { this.numeCaz = numeCaz; }
    public Double getSumaTotala() { return sumaTotala; }
    public void setSumaTotala(Double sumaTotala) { this.sumaTotala = sumaTotala; }
}