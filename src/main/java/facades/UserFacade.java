package facades;

import dtos.*;
import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import errorhandling.InvalidInputException;
import errorhandling.NotFoundException;
import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;
    private static ButikFacade butikFacade;
    private static ProduktFacade produktFacade;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
            butikFacade = ButikFacade.getButikFacade(emf);
            produktFacade = ProduktFacade.getProduktFacade(emf);
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User getUser(String username){
        EntityManager em = emf.createEntityManager();
        User user;
        user = em.find(User.class, username);
        return user;
    }

    public List<UserDTO> getKunderByButik(String navn){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select u from Butik b join b.users u where b.navn = :navn", User.class);
            query.setParameter("navn", navn);
            List<User> ue = query.getResultList();
            return UserDTO.getDtos(ue);
        } finally {
            em.close();
        }
    }

    public List<PrisProduktDTO> getPrisByKunde(String kunde){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select p.id, p.beløb, pp.navn, b.navn from Pris p join Butik b on b.id = p.butik.id join User u on u.butik.id = b.id join Produkt pp on pp.id = p.produkt.id where u.userName = :kunde", Pris.class);
            query.setParameter("kunde", kunde);
            System.out.println(query.getResultList());
            List<PrisProduktDTO> pe = query.getResultList();
            return pe;
        } finally {
            em.close();
        }
    }

    public UserDTO createKunde(UserDTO userdto) throws AuthenticationException, InvalidInputException {
        EntityManager em = emf.createEntityManager();
        List<Role> role = new ArrayList<>();
        Role userRole = new Role("user");
        role.add(userRole);

        User user =  new User(userdto.getUserName(), userdto.getUserPass(),role);

        try {

            em.getTransaction().begin();
            Butik butik = butikFacade.getOrCreateButik(userdto.getButikDTO());
            Produkt produkt = produktFacade.getProduktByName("Fremvisning");
            System.out.println("produkt: " + produkt.getNavn());
            em.persist(user);
            butik.addUser(user);
            user.addProdukter(produkt);
            em.merge(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }
    public User getKundeByName(String name){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery query = em.createQuery("select u from User u where u.userName = :name", User.class);
            query.setParameter("name", name);
            System.out.println(query.getSingleResult());
            User user = (User) query.getSingleResult();
            return user;
        }
        catch (NoResultException nre){
            return null;
        } finally {
            em.close();
        }
    }

    public UserDTO sendRequest(UserDTO userDTO, ProduktDTO produktDTO){
        EntityManager em = emf.createEntityManager();
        User user = getKundeByName(userDTO.getUserName());
        Produkt produkt = produktFacade.getProduktByName(produktDTO.getNavn());
        System.out.println("produkt " + produkt.getNavn());

        try{
            em.getTransaction().begin();
            user.addProdukter(produkt);
            em.merge(user);
            System.out.println("kunde --> " + produkt.getKunder().size());
            em.getTransaction().commit();
            return new UserDTO(user);
        } finally {
            em.close();
        }
    }
}
