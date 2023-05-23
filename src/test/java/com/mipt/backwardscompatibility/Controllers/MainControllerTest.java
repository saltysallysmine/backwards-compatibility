package com.mipt.backwardscompatibility.Controllers;

import com.mipt.backwardscompatibility.Service.Requests.RequestV1;
import com.mipt.backwardscompatibility.Service.Requests.RequestV4;
import com.mipt.backwardscompatibility.Service.Requests.RequestV5;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV1;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV2;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV3;
import com.mipt.backwardscompatibility.Service.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static MainController mainController;

    @BeforeAll
    static void InitializeClient() {
        mainController = new MainController();
        mainController.setUsersRepository(Set.of(
                new User("ANDREY", "Andrey", "Shinkarenko", "Vladimirovich", 18),
                new User("ANDreY", "Andrey", "Menelaevich", null, 19),
                new User("KONSTANTIN", "Konstantin", "Bolshikov", "Andreevich", 19),
                new User("VALERY", "Valery", "Bergman", "Dmitrievich", 18)));
    }

    @Test
    public void getRegexOfTest() {
        String likeString = ".* Something_like_%";
        String expectedString = "\\.\\* Something.like..*";
        assertEquals(expectedString, mainController.getRegexOf(likeString));
    }

    public void getUsersAndAssertEqualsV1(String likeString) {
        RequestV1 requestV1 = new RequestV1(likeString);
        ResponseV1 response = mainController.getUsersV1(requestV1);
        Set<ResponseV1.UserV1> foundUsers = new HashSet<>(Set.of(
                new ResponseV1.UserV1("ANDREY"),
                new ResponseV1.UserV1("ANDreY")));
        if (likeString == null) {
            foundUsers.add(new ResponseV1.UserV1("KONSTANTIN"));
            foundUsers.add(new ResponseV1.UserV1("VALERY"));
        }
        ResponseV1 expectedResponse = new ResponseV1(foundUsers);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void getUserV1Test() {
        // Request with _
        getUsersAndAssertEqualsV1("AND__Y");
        // Request with %
        getUsersAndAssertEqualsV1("AND%Y");
        // Request with null
        getUsersAndAssertEqualsV1(null);
    }

    public void getUsersAndAssertEqualsV2(String likeString) {
        RequestV1 requestV1 = new RequestV1(likeString);
        ResponseV2 response = mainController.getUsersV2(requestV1);
        Set<ResponseV2.UserV2> foundUsers = new HashSet<>(Set.of(
                new ResponseV2.UserV2("ANDREY"),
                new ResponseV2.UserV2("ANDreY")));
        if (likeString == null) {
            foundUsers.add(new ResponseV2.UserV2("KONSTANTIN"));
            foundUsers.add(new ResponseV2.UserV2("VALERY"));
        }
        ResponseV2 expectedResponse = new ResponseV2(4, foundUsers);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void getUserV2Test() {
        // Request with _
        getUsersAndAssertEqualsV2("AND__Y");
        // Request with %
        getUsersAndAssertEqualsV2("AND%Y");
        // Request with null
        getUsersAndAssertEqualsV2(null);
    }

    public void getUsersAndAssertEqualsV3(String likeString) {
        RequestV1 requestV1 = new RequestV1(likeString);
        ResponseV3 response = mainController.getUsersV3(requestV1);
        Set<ResponseV3.UserV3> foundUsers = new HashSet<>(Set.of(
                new ResponseV3.UserV3("ANDREY", "Andrey", "Shinkarenko", "Vladimirovich"),
                new ResponseV3.UserV3("ANDreY", "Andrey", "Menelaevich", null)));
        if (likeString == null) {
            foundUsers.add(new ResponseV3.UserV3(
                    "KONSTANTIN", "Konstantin", "Bolshikov", "Andreevich"));
            foundUsers.add(new ResponseV3.UserV3(
                    "VALERY", "Valery", "Bergman", "Dmitrievich"));
        }
        ResponseV3 expectedResponse = new ResponseV3(4, foundUsers);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void getUserV3Test() {
        // Request with _
        getUsersAndAssertEqualsV3("AND__Y");
        // Request with %
        getUsersAndAssertEqualsV3("AND%Y");
        // Request with null
        getUsersAndAssertEqualsV3(null);
    }

    public void getUsersAndAssertEqualsV4(String likeString, String regexString) {
        RequestV4 requestV4 = new RequestV4(likeString, regexString);
        ResponseV3 response = mainController.getUsersV4(requestV4);
        Set<ResponseV3.UserV3> foundUsers = new HashSet<>(Set.of());
        if (likeString == null && regexString == null) {
            // all users
            foundUsers.add(new ResponseV3.UserV3(
                    "ANDREY", "Andrey", "Shinkarenko", "Vladimirovich"));
            foundUsers.add(new ResponseV3.UserV3(
                    "ANDreY", "Andrey", "Menelaevich", null));
            foundUsers.add(new ResponseV3.UserV3(
                    "VALERY", "Valery", "Bergman", "Dmitrievich"));
            foundUsers.add(new ResponseV3.UserV3(
                    "KONSTANTIN", "Konstantin", "Bolshikov", "Andreevich"));
        } else if (regexString == null) {
            // only matches to likeString
            foundUsers.add(new ResponseV3.UserV3(
                    "ANDREY", "Andrey", "Shinkarenko", "Vladimirovich"));
            foundUsers.add(new ResponseV3.UserV3(
                    "ANDreY", "Andrey", "Menelaevich", null));
            foundUsers.add(new ResponseV3.UserV3(
                    "VALERY", "Valery", "Bergman", "Dmitrievich"));
        } else {
            // only matches to regexString
            foundUsers.add(new ResponseV3.UserV3(
                    "KONSTANTIN", "Konstantin", "Bolshikov", "Andreevich"));
        }
        ResponseV3 expectedResponse = new ResponseV3(4, foundUsers);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void getUserV4Test() {
        /* In these examples:
         * - likeString matches to ANDREY, ANDreY and VALERY;
         * - but regexString will only match to KONSTANTIN.
         */
        // Assert priority of regex
        getUsersAndAssertEqualsV4("%__Y", "[JDK]+ON.*");
        // Assert null likeString
        getUsersAndAssertEqualsV4(null, "[JDK]+ON.*");
        // Assert null regexString
        getUsersAndAssertEqualsV4("%__Y", null);
        // Assert all nulls
        getUsersAndAssertEqualsV4(null, null);
    }

    public void getUsersAndAssertEqualsV5(RequestV5 requestV5) {
        ResponseV3 response = mainController.getUsersV5(requestV5);
        Set<ResponseV3.UserV3> foundUsers = new HashSet<>(Set.of());
        foundUsers.add(new ResponseV3.UserV3(
                "VALERY", "Valery", "Bergman", "Dmitrievich"));
        ResponseV3 expectedResponse = new ResponseV3(4, foundUsers);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void getUserV5Test() {
        /* In these example:
         * - regexString matches to ANDREY, ANDreY and VALERY;
         * - left and right age borders matches to ANDREY and VALERY (of the remaining);
         * - surname matches to VALERY.
         */
        // Assert results merge
        getUsersAndAssertEqualsV5(new RequestV5(
                "[A-Za-z]+Y", "Bergman", 7, 18));
    }

}