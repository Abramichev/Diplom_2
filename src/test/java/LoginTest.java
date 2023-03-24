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
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest {
    private String email = "qwerty@qwe.ru";
    private String password = "2345";
    private String name = "simpson";
    private String wrongEmail = "wrong@wrong.com";
    private String wrongPassword = "wrong12345";
    private static Logger logger = LoggerFactory.getLogger(LoginTest.class);

    @Before
    public void setUp() {
        BaseSetUp.setUp();
        UserApi.createUser(email, password, name);
    }

    @Test
    @DisplayName("Check status code of POST /api/auth/login")
    public void successfulLoginShouldReturn200Test() {
        Response response = UserApi.login(email, password);
        response
                .then()
                .assertThat()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check body of POST /api/auth/login")
    public void successfulLoginShouldReturnValidBodyTest() {
        Response response = UserApi.login(email, password);
        response
                .body()
                .as(SuccessUserCreationResponse.class);
    }

    @Test
    @DisplayName("Check status code and body of POST /api/auth/login with wrong data")
    public void loginWithWrongDataShouldReturn401WithValidBodyTest() {
        Response response = UserApi.login(wrongEmail, wrongPassword);
        response
                .then()
                .assertThat()
                .body("message", equalTo("email or password are incorrect")).
                and()
                .statusCode(SC_UNAUTHORIZED);
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
