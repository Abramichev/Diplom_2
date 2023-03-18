import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.utils.BaseSetUp;
import stellarburgers.utils.OrderApi;
import stellarburgers.utils.UserApi;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class GetUserOrdersTest {



    String[] ingredients = {"61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa71"};

    String email = "qwerty@qwe.ru";
    String password = "2345";
    String name = "simpson";

    @Before
    public void setUp() {
        BaseSetUp.setUp();
        UserApi.createUser(email, password, name);
        OrderApi.createOrderWithAuth(ingredients, email, password, name);
    }

    @Test
    @DisplayName("Check status code and body of GET /api/orders")
    public void getUserOrdersWithAuthShouldReturn200ФAndValidTodayAmountOfOrdersTest() {
        Response response = OrderApi.getUserOrdersWithAuth(email, password);
        response.then().assertThat().statusCode(SC_OK).and().body("totalToday", equalTo(1));
    }

    @Test
    @DisplayName("Check status code and body of GET /api/orders without auth")
    public void getUserOrdersWithoutAuthShouldReturn401AndValidBodyTest() {
        Response response = OrderApi.getUserOrdersWithoutAuth();
        response.then().assertThat().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        try {
            UserApi.deleteUser(email, password);
        } catch (IllegalArgumentException exception) {
            System.out.println("Невозможно удалить несуществующего пользователя!");
        }
    }
}
