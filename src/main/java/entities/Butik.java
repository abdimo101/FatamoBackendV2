package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "butik")
@Entity
@NamedQuery(name = "butik.deleteAllRows", query = "DELETE from Butik b")
public class Butik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String navn;

    @OneToMany(mappedBy = "butik", cascade = CascadeType.PERSIST)
    private List<User> users;

    @OneToMany(mappedBy = "butik", cascade = CascadeType.PERSIST)
    private  List<Pris> priser;

    public Butik() {
    }

    public Butik(String navn) {
        this.navn = navn;
        this.users = new ArrayList<>();
        this.priser = new ArrayList<>();
    }

    public List<User> getKunder() {
        return users;
    }

    public void addUser(User user)
    {
        this.users.add(user);
        if(user != null){
            user.setButik(this);
        }
    }

    public List<Pris> getPriser() {
        return priser;
    }

    public void addPriser(Pris pris)
    {
        this.priser.add(pris);
        if(pris != null){
            pris.setButik(this);
        }
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
}