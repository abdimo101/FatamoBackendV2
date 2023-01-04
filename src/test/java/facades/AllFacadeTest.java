package facades;

import dtos.*;
import entities.*;
import errorhandling.NotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import org.junit.jupiter.api.Order;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class AllFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade userFacade;
    private static ProduktFacade produktFacade;
    private static ButikFacade butikFacade;
    private static AdministratorFacade administratorFacade;

    public AllFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        userFacade = UserFacade.getUserFacade(emf);
        produktFacade = ProduktFacade.getProduktFacade(emf);
        administratorFacade = AdministratorFacade.getAdministratorFacade(emf);
        butikFacade = ButikFacade.getButikFacade(emf);

    }

    @AfterAll
    public static void tearDownClass() {}

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("user.deleteAllRows").executeUpdate();
            em.createNamedQuery("pris.deleteAllRows").executeUpdate();
            em.createNamedQuery("produkt.deleteAllRows").executeUpdate();
            em.createNamedQuery("butik.deleteAllRows").executeUpdate();
            Produkt produkt = new Produkt("Fremvisning");
            Butik butik = new Butik("EDC");
            Pris pris = new Pris(1,1000, butik, produkt);
            User user = new User("testkunde0", "123");
            User user1 = new User("testkunde01","123");
            butik.addUser(user);
            butik.addUser(user1);
            em.persist(user);
            em.persist(user1);
            em.persist(butik);
            em.persist(produkt);
            em.persist(pris);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }



    @AfterEach
    public void tearDown() {


    }


    @Test
    public void testCreateUser() throws Exception{
        List<Role> role = new ArrayList<>();
        Role userRole = new Role("user");
        Butik butik = new Butik("Home");
        role.add(userRole);
        User user = new User("testkunde", "123", role);
        butik.addUser(user);
        UserDTO userdto = new UserDTO(user);

        assertEquals("testkunde", userFacade.createKunde(userdto).getUserName());
    }

    @Test
    void testCreatePris() throws NotFoundException {
        ProduktDTO produktDTO = new ProduktDTO("Fremvisning");
        ButikDTO butikDTO = new ButikDTO("Home");
        PrisDTO prisDTO = new PrisDTO(3000, butikDTO, produktDTO);
        PrisDTO res = administratorFacade.createPris(prisDTO);
        assertEquals(3000, res.getBeløb());
    }

    //@Test
    void testEditPris() throws NotFoundException {

        testCreatePris();
        ProduktDTO produktDTO = new ProduktDTO("Fremvisning");
        ButikDTO butikDTO = new ButikDTO("Home");
        PrisDTO prisDTO = new PrisDTO(2500, butikDTO, produktDTO);
        prisDTO.setId(1);
        PrisDTO res = administratorFacade.editPris(prisDTO);
        assertEquals(2500, res.getBeløb());
    }

    @Test
    public void testGetPrisByKunde()  {
        List<PrisProduktDTO> priser = userFacade.getPrisByKunde("testkunde0");
        assertEquals(1, priser.size());
    }

    @Test
    public void testSendForespørgsel() throws Exception{
        User user = userFacade.getKundeByName("testkunde0");
        UserDTO userDTO = new UserDTO(user);
        Produkt produkt = produktFacade.getProduktByName("Fremvisning");
        ProduktDTO produktDTO = new ProduktDTO(produkt);
        userFacade.sendRequest(userDTO, produktDTO);
        assertEquals(0, produktDTO.getUsers().size());
    }

    @Test

    public void testGetKunderByButik()  {
        List<UserDTO> users = userFacade.getKunderByButik("EDC");
        assertEquals(2, users.size());
    }


    @Test
    public void testGetKundeByName()  {
        User user = userFacade.getKundeByName("testkunde0");
        assertEquals("EDC", user.getButik().getNavn());
    }

   @Test
   public void testDeleteKunde() throws NotFoundException {
       administratorFacade.deleteKunde("testkunde01");
       assertNull(userFacade.getKundeByName("testkunde01"));
   }






}

