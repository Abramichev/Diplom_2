package stellarburgers.config;

import io.restassured.RestAssured;

import static stellarburgers.utils.ApiUrls.BASE_URL;

public class BaseSetUp {
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

}
