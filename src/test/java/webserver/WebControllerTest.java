package webserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import webserver.RequestClasses.Credentials;
import webserver.ResponseClasses.BooleanOutput;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import java.util.Enumeration;

import static org.junit.Assert.*;

@SpringBootTest("Mock")
public class WebControllerTest {

    WebController controller;
    HttpSession session;
    HttpServletResponse response;
    Credentials credentials;

    @Before
    public void setUp(HttpSession session, HttpServletResponse response) throws Exception {
        this.session = session;
        this.response = response;
        controller = new WebController();
        controller.resetall(session, response);
        credentials = new Credentials("DefaultUser", "DefaultPassword");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testLogin() throws Exception {
        assertEquals(controller.login(session, response, credentials), new BooleanOutput(true));
    }

    @Test
    public void testChangeCredentials() throws Exception {
        controller.login(session, response, credentials);
        Credentials newCredentials = new Credentials("ChangedUser", "ChangedPassword");
        assertEquals(controller.changecredentials(session, response, newCredentials), true);
    }
}
