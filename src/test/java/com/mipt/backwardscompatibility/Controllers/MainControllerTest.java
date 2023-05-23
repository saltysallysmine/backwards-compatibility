package com.mipt.backwardscompatibility.Controllers;

import com.mipt.backwardscompatibility.Service.Requests.RequestV1;
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
                new User("ANDreY", "Andrey", "Menelaevich", null, 18),
                new User("KONSTANTIN", "Konstantin", "Bolshikov", "Andreevich", 19),
                new User("VALERY", "Valery", "Bergman", "Dmitrievich", 18)));
    }

    @Test
    public void getRegexOfTest() {
        String likeString = ".* Something_like_%";
        String expectedString = "\\.\\* Something.like..*";
        assertEquals(mainController.getRegexOf(likeString), expectedString);
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
        assertEquals(response, expectedResponse);
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
        assertEquals(response, expectedResponse);
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
                    "KONSTANTIN", "Konstantin","Bolshikov", "Andreevich"));
            foundUsers.add(new ResponseV3.UserV3(
                    "VALERY", "Valery", "Bergman", "Dmitrievich"));
        }
        ResponseV3 expectedResponse = new ResponseV3(4, foundUsers);
        assertEquals(response, expectedResponse);
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
                new ResponseV3.UserV3("ANDREY", "Andrey", "Shinkarenko", "Vladimirovich"),
                new ResponseV3.UserV3("ANDreY", "Andrey", "Menelaevich", null));
        ResponseV3 expectedResponse = new ResponseV3(4, foundUsers);
        assertEquals(response, expectedResponse);
    }

}