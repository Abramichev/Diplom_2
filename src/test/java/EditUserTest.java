import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellarburgers.config.BaseSetUp;
import stellarburgers.utils.UserApi;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class EditUserTest {
    String email = "qwerty@qwe.ru";
    String password = "2345";
    String name = "simpson";
    String newName = "homer simpson";
    String wrongEmail = "wrong@wrong.com";
    String wrongPassword = "wrong12345";

    @Before
    public void setUp() {
        BaseSetUp.setUp();
        UserApi.createUser(email, password, name);
    }

    @Test
    @DisplayName("Check status code and body of PATCH /api/auth/user")
    public void editAuthorizedUserShouldReturn200AndUpdatedData() {
        Response response = UserApi.editUser(email, password, newName);
        response
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Check status code and body of PATCH /api/auth/user without auth")
    public void editUnauthorizedUserShouldReturn401WithValidBodyTest() {
        Response response = UserApi.editUser(wrongEmail, wrongPassword, newName);
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
            System.out.println("Невозможно удалить несуществующего пользователя!");
        }
    }
}
