import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.stellar.burgers.responses.SuccessUserCreationResponse;
import ru.stellar.burgers.config.BaseSetUp;
import ru.stellar.burgers.utils.UserApi;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUserTest {
    private String email = "qwerty@qwe.ru";
    private String password = "2345";
    private String name = "simpson";

    private static Logger logger = LoggerFactory.getLogger(CreateUserTest.class);
    @Before
    public void setUp() {
        BaseSetUp.setUp();
    }

    @Test
    @DisplayName("Check status code of POST /api/auth/register")
    public void successfulCreationShouldReturn200Test() {
        Response response = UserApi.createUser(email, password, name);
        response
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true),
                "accessToken", notNullValue());

    }

    @Test
    @DisplayName("Check status code and body of POST /api/auth/register with double register")
    public void doubleCreationWithTheSameDataShouldReturn403WithValidMessageTest() {
        UserApi.createUser(email, password, name);
        Response response = UserApi.createUser(email, password, name);
        response
                .then()
                .assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);

    }

//    @Test
//    @DisplayName("Check body of POST /api/auth/register")
//    public void successfulCreationShouldReturnValidBodyTest() {
//        Response response = UserApi.createUser(email, password, name);
//        response
//                .body()
//                .as(SuccessUserCreationResponse.class);
//    }


    @Test
    @DisplayName("Check status code and body of POST /api/auth/register without name")
    public void doubleCreationWithoutNameShouldReturn403WithValidMessageTest() {
        Response response = UserApi.createUserWithoutName(email, password);
        response
                .then()
                .assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
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
