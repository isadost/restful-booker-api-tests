package io.github.isadost.tests;

import io.github.isadost.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

class BookingApiTest {

    private static final String BOOKING_REQUEST = """
            {
              "firstname": "Isa",
              "lastname": "Dost",
              "totalprice": 1250,
              "depositpaid": true,
              "bookingdates": {
                "checkin": "2026-09-10",
                "checkout": "2026-09-15"
              },
              "additionalneeds": "Breakfast"
            }
            """;

    private static final String UPDATED_BOOKING_REQUEST = """
            {
              "firstname": "Isa",
              "lastname": "Dost",
              "totalprice": 1500,
              "depositpaid": true,
              "bookingdates": {
                "checkin": "2026-09-10",
                "checkout": "2026-09-16"
              },
              "additionalneeds": "Breakfast and late checkout"
            }
            """;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET /booking rezervasyon listesini başarıyla döndürmeli")
    void shouldGetBookingListSuccessfully() {
        given()
                .accept(ContentType.JSON)
        .when()
                .get("/booking")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", not(empty()));
    }

    @Test
    @Tag("crud")
    @DisplayName("POST ile oluşturulan rezervasyon GET ile okunabilmeli")
    void shouldCreateAndGetBookingSuccessfully() {
        int bookingId = createBooking();

        given()
                .pathParam("bookingId", bookingId)
        .when()
                .get("/booking/{bookingId}")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("firstname", equalTo("Isa"))
                .body("lastname", equalTo("Dost"))
                .body("totalprice", equalTo(1250))
                .body("bookingdates.checkout", equalTo("2026-09-15"));
    }

    @Test
    @Tag("crud")
    @DisplayName("Rezervasyon oluşturma, güncelleme ve silme akışı çalışmalı")
    void shouldCompleteBookingLifecycleSuccessfully() {
        int bookingId = createBooking();
        String token = createAuthToken();

        given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .pathParam("bookingId", bookingId)
                .body(UPDATED_BOOKING_REQUEST)
        .when()
                .put("/booking/{bookingId}")
        .then()
                .statusCode(200)
                .body("totalprice", equalTo(1500))
                .body("bookingdates.checkout", equalTo("2026-09-16"))
                .body("additionalneeds", equalTo("Breakfast and late checkout"));

        given()
                .cookie("token", token)
                .pathParam("bookingId", bookingId)
        .when()
                .delete("/booking/{bookingId}")
        .then()
                .statusCode(201);

        given()
                .pathParam("bookingId", bookingId)
        .when()
                .get("/booking/{bookingId}")
        .then()
                .statusCode(404);
    }

    private int createBooking() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(BOOKING_REQUEST)
        .when()
                .post("/booking")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("bookingid", greaterThan(0))
                .body("booking.firstname", equalTo("Isa"))
                .body("booking.lastname", equalTo("Dost"))
                .extract()
                .path("bookingid");
    }

    private String createAuthToken() {
        String authRequest = """
                {
                  "username": "admin",
                  "password": "password123"
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(authRequest)
        .when()
                .post("/auth")
        .then()
                .statusCode(200)
                .body("token", not(empty()))
                .extract()
                .response();

        return response.path("token");
    }
}
