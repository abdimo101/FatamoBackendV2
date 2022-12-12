package facades;

import dtos.ProduktDTO;
import dtos.UserDTO;
import entities.Butik;
import entities.Produkt;
import entities.Role;
import entities.User;
import errorhandling.NotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade userFacade;
    private static ProduktFacade produktFacade;
    private static AdministratorFacade administratorFacade;

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        userFacade = UserFacade.getUserFacade(emf);
        produktFacade = ProduktFacade.getProduktFacade(emf);
        administratorFacade = AdministratorFacade.getAdministratorFacade(emf);

    }

    @AfterAll
    public static void tearDownClass() {
        //Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }


    @AfterEach
    public void tearDown() {


    }

    // TODO: Delete or change this method
/*
    @Test
    public void testCreateUser() throws Exception{
        List<Role> role = new ArrayList<>();
        Role userRole = new Role("user");
        Butik butik = new Butik("Home");
        role.add(userRole);
        User John = new User("ffs", "THansen", role);
        butik.addUser(John);
        UserDTO John1 = new UserDTO(John);

        assertEquals("ffs", userFacade.createKunde(John1).getUserName());
    }

    @Test
    public void testSendForespørgsel() throws Exception{
        User user = new User("userp2", "123");
        UserDTO userDTO = new UserDTO(user);
        Produkt produkt = new Produkt("Fremvisning");
        ProduktDTO produktDTO = new ProduktDTO(produkt);
        userFacade.sendRequest(userDTO, produktDTO);
        assertEquals("1", userDTO.getProdukter().size());
    }
*/

   @Test
   public void testDeleteKunde() throws NotFoundException {
       administratorFacade.deleteKunde("ffs");
       assertNull(userFacade.getKundeByName("ffs"));
   }
}