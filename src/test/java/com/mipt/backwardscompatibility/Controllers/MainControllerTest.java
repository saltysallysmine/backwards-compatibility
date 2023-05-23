package com.mipt.backwardscompatibility.Controllers;

import com.mipt.backwardscompatibility.Service.Requests.RequestV1;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV1;
import com.mipt.backwardscompatibility.Service.Responses.ResponseV2;
import com.mipt.backwardscompatibility.Service.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

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

    @Test
    public void getUserV1Test1() {
        // Request with _
        String likeString = "AND__Y";
        RequestV1 requestV1 = new RequestV1(likeString);
        ResponseV1 response = mainController.getUsersV1(requestV1);
        Set<ResponseV1.UserV1> foundUsers = Set.of(
                new ResponseV1.UserV1("ANDREY"),
                new ResponseV1.UserV1("ANDreY"));
        ResponseV1 expectedResponse = new ResponseV1(foundUsers);
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getUserV1Test2() {
        // Request with %
        String likeString = "AND%Y";
        RequestV1 requestV1 = new RequestV1(likeString);
        ResponseV1 response = mainController.getUsersV1(requestV1);
        Set<ResponseV1.UserV1> foundUsers = Set.of(
                new ResponseV1.UserV1("ANDREY"),
                new ResponseV1.UserV1("ANDreY"));
        ResponseV1 expectedResponse = new ResponseV1(foundUsers);
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getUserV2Test1() {
        // Request with _
        String likeString = "AND__Y";
        RequestV1 requestV1 = new RequestV1(likeString);
        ResponseV2 response = mainController.getUsersV2(requestV1);
        Set<ResponseV2.UserV2> foundUsers = Set.of(
                new ResponseV2.UserV2("ANDREY"),
                new ResponseV2.UserV2("ANDreY"));
        ResponseV2 expectedResponse = new ResponseV2(4, foundUsers);
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getUserV2Test2() {
        // Request with %
        String likeString = "AND%Y";
        RequestV1 requestV1 = new RequestV1(likeString);
        ResponseV2 response = mainController.getUsersV2(requestV1);
        Set<ResponseV2.UserV2> foundUsers = Set.of(
                new ResponseV2.UserV2("ANDREY"),
                new ResponseV2.UserV2("ANDreY"));
        ResponseV2 expectedResponse = new ResponseV2(4, foundUsers);
        assertEquals(response, expectedResponse);
    }


}