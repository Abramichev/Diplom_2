package ru.stellar.burgers.config;

import io.restassured.RestAssured;

import static ru.stellar.burgers.utils.ApiUrls.BASE_URL;

public class BaseSetUp {
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

}
