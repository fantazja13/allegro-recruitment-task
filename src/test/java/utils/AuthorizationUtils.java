package utils;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;

public class AuthorizationUtils {

    public static final String CLIENT_ID_KEY = "authorization.app.credentials.client_id";
    public static final String CLIENT_SECRET_KEY = "authorization.app.credentials.client_secret";
    public static final String URI_BASE_KEY = "authorization.uri.base";
    public static final String URI_ENDPOINT_KEY = "authorization.uri.token_endpoint";

    public static String getAccessTokenForApplication() {
        return given()
                .auth()
                .basic(PropertiesUtils.getPropertyValue(CLIENT_ID_KEY),
                        PropertiesUtils.getPropertyValue(CLIENT_SECRET_KEY))
                .contentType(URLENC)
                .queryParam("grant_type", "client_credentials")
                .post(authUri())
                .then()
                .extract()
                .path("access_token");
    }

    private static String authUri() {
        return PropertiesUtils.getPropertyValue(URI_BASE_KEY) + PropertiesUtils.getPropertyValue(URI_ENDPOINT_KEY);
    }
}
