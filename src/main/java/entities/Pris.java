package entities;

import javax.persistence.*;

@Table(name = "pris")
@Entity
public class Pris {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer beløb;
    @ManyToOne()
    private Butik butik;
    @ManyToOne()
    private Produkt produkt;

    public Pris() {
    }

    public Pris(Integer id,Integer beløb, Butik butik, Produkt produkt) {
        this.id = id;
        this.beløb = beløb;
        this.butik = butik;
        this.produkt = produkt;
    }

    public Integer getBeløb() {
        return beløb;
    }

    public void setBeløb(Integer beløb) {
        this.beløb = beløb;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Butik getButik() {
        return butik;
    }

    public void setButik(Butik butik) {
        this.butik = butik;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }
}