package stellarburgers.utils;

import io.restassured.response.Response;
import stellarburgers.objects.User;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static stellarburgers.utils.ApiHandlers.*;

public class UserApi {

    public static Response createUser(String email, String password, String name) {
        User user = new User(email, password, name);
        return given()
                .header("Content-type", JSON)
                .and()
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    public static Response createUserWithoutName(String email, String password) {
        User user = new User(email, password);
        return given()
                .header("Content-type", JSON)
                .and()
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    public static Response login(String email, String password) {
        User user = new User(email, password);
        return given()
                .header("Content-type", JSON)
                .and()
                .body(user)
                .when()
                .post(LOGIN);
    }

    public static Response editUser(String email, String password, String newName) {
        String token = getToken(email, password);
        User user = new User(newName);
        return given()
                .headers("Content-type", JSON, "Authorization", token)
                .and()
                .body(user)
                .when()
                .patch(EDIT_USER);
    }

    public static Response getUser(String email, String password) {
        String token = getToken(email, password);
        return given()
                .header("Authorization", token)
                .when()
                .get(EDIT_USER);
    }

    public static String getToken(String email, String password) {
        Response response = login(email, password);
        return response
                .then()
                .extract()
                .body()
                .path("accessToken");
    }

    public static Response deleteUser(String email, String password) {
        String token = getToken(email, password);
        return given()
                .header("Authorization", token)
                .when()
                .delete(EDIT_USER);

    }
}
