package rest;

import dtos.*;
import entities.*;
import errorhandling.NotFoundException;
import io.restassured.http.ContentType;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class AllResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
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

    @Test
    public void testServerIsUp() {
        given().when().get("/xxx").then().statusCode(200);
    }

    @Test
    public void testCreateKunde() {
        List<Role> role = new ArrayList<>();
        Role userRole = new Role("user");
        Butik butik = new Butik("Home");
        role.add(userRole);
        User user = new User("testkunde", "123", role);
        butik.addUser(user);
        UserDTO userdto = new UserDTO(user);

        given().
                contentType("application/json").
                body(userdto)
                .when()
                .request("post", "/user").then()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }

    @Test
    public void testCreatePris() {
        ProduktDTO produktDTO = new ProduktDTO("Fremvisning");
        ButikDTO butikDTO = new ButikDTO("Home");
        PrisDTO prisDTO = new PrisDTO(3000, butikDTO, produktDTO);
        given().
                contentType("application/json").
                body(prisDTO)
                .when()
                .request("post", "/admin").then()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }

  //  @Test
    void testEditPris() throws NotFoundException {

        testCreatePris();
        ProduktDTO produktDTO = new ProduktDTO("Fremvisning");
        ButikDTO butikDTO = new ButikDTO("Home");
        PrisDTO prisDTO = new PrisDTO(2500, butikDTO, produktDTO);
        prisDTO.setId(1);
        given().
                contentType("application/json").
                body(prisDTO)
                .when()
                .request("put", "/admin/pris/edit/"+prisDTO.getId()).then()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }

}
