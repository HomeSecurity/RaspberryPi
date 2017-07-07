package webserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import webserver.RequestClasses.Credentials;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.junit.Assert.*;

//EXAMPLE TEST CASES FOR THE WEBCONTROLLER
public class WebControllerTest {

    WebController controller;
    HttpSession session;
    HttpServletResponse response;
    Credentials credentials;

    //create a mocked session and response
    //initialize the webcontroller and the credentials
    @Before
    public void setUp() throws Exception {
        this.session = new MockHttpSession();
        this.response = new MockHttpServletResponse();
        this.credentials = new Credentials("DefaultUser", "DefaultPassword");
        this.controller = new WebController();

        //initial login
        if (!controller.login(session, response, credentials).getOutput()) {
            System.err.println("Not logged in");
        }
    }

    @After
    public void tearDown() throws Exception {
        //reset all data for following tests
        controller.resetall(session, response);
    }

    @Test
    public void testLogin() throws Exception {
        //this is already done in the startup method but a assert is nevertheless needed
        assertTrue(controller.login(session, response, credentials).getOutput());
    }

    @Test
    public void testChangeCredentials() throws Exception {
        Credentials newCredentials = new Credentials("ChangedUser", "ChangedPassword");
        assertTrue(controller.changecredentials(session, response, newCredentials).getOutput());
    }
}
