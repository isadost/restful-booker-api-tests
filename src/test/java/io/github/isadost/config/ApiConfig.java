package io.github.isadost.config;

public final class ApiConfig {

    public static final String BASE_URL = System.getenv()
            .getOrDefault("API_BASE_URL", "https://restful-booker.herokuapp.com");

    private ApiConfig() {
        // Bu sınıftan nesne oluşturulmasını engeller.
    }
}
