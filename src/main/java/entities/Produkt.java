package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "produkt")
@Entity
public class Produkt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String navn;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<User> users;
    @OneToMany(mappedBy = "produkt",cascade = CascadeType.PERSIST)
    private List<Pris> priser;

    public Produkt() {
    }

    public Produkt(String navn) {
        this.navn = navn;
        this.users = new ArrayList<>();
        this.priser = new ArrayList<>();
    }



    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<User> getKunder() {
        return users;
    }

    public void setKunder(List<User> users) {
        this.users = users;
    }

    public List<Pris> getPriser() {
        return priser;
    }

    public void addPriser(Pris pris)
    {
        this.priser.add(pris);
        if(pris != null){
            pris.setProdukt(this);
        }
    }
}