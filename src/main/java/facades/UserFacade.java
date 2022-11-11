package facades;

import dtos.UserDTO;
import entities.Butik;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import errorhandling.InvalidInputException;
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

    public UserDTO createUser(UserDTO userdto) throws AuthenticationException, InvalidInputException {
        EntityManager em = emf.createEntityManager();
        List<Role> role = new ArrayList<>();
        Role userRole = new Role("user");
        role.add(userRole);
        User user =  new User(userdto.getUserName(), userdto.getUserPass(),role);

        try {

            em.getTransaction().begin();
            Butik butik = butikFacade.getOrCreateButik(userdto.getButikDTO());
            em.persist(user);
            butik.addUser(user);
            em.merge(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }

}
