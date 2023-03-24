import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stellar.burgers.config.BaseSetUp;
import ru.stellar.burgers.utils.OrderApi;
import ru.stellar.burgers.utils.UserApi;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserOrdersTest {
    private String[] ingredients = {"61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa71"};
    private String email = "qwerty@qwe.ru";
    private String password = "2345";
    private String name = "simpson";

    private static Logger logger = LoggerFactory.getLogger(GetUserOrdersTest.class);
    @Before
    public void setUp() {
        BaseSetUp.setUp();
        UserApi.createUser(email, password, name);
        OrderApi.createOrderWithAuth(ingredients, email, password, name);
    }

    @Test
    @DisplayName("Check status code and body of GET /api/orders")
    public void getUserOrdersWithAuthShouldReturn200AndValidTodayAmountOfOrdersTest() {
        Response response = OrderApi.getUserOrdersWithAuth(email, password);
        response
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Check status code and body of GET /api/orders without auth")
    public void getUserOrdersWithoutAuthShouldReturn401AndValidBodyTest() {
        Response response = OrderApi.getUserOrdersWithoutAuth();
        response
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        try {
            UserApi.deleteUser(email, password);
        } catch (IllegalArgumentException exception) {
            logger.error("Невозможно удалить несуществующего пользователя!");
        }
    }
}
