package ru.stellar.burgers.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellar.burgers.objects.IngredientsRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static ru.stellar.burgers.utils.ApiHandlers.ORDER;

public class OrderApi {

    @Step ("createOrderWithoutAuth")
    public static Response createOrderWithoutAuth(String[] ingredients) {
        IngredientsRequest ingredientsRequest = new IngredientsRequest(ingredients);
        return given()
                .header("Content-type", JSON)
                .and()
                .body(ingredientsRequest)
                .when()
                .post(ORDER);
    }
    @Step("createOrderWithAuth")
    public static Response createOrderWithAuth(String[] ingredients, String email, String password, String name) {
        IngredientsRequest ingredientsRequest = new IngredientsRequest(ingredients);
        UserApi.createUser(email, password, name);
        String token = UserApi.getToken(email, password);
        return given().
                headers("Content-type", JSON, "Authorization", token)
                .and()
                .body(ingredientsRequest)
                .when()
                .post(ORDER);
    }
    @Step("getUserOrdersWithAuth")
    public static Response getUserOrdersWithAuth(String email, String password) {
        String token = UserApi.getToken(email, password);
        return given()
                .headers("Content-type", JSON, "Authorization", token)
                .when()
                .get(ORDER);
    }

    public static Response getUserOrdersWithoutAuth() {
        return given()
                .header("Content-type", JSON)
                .when()
                .get(ORDER);
    }
}
