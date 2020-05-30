import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AuthorizationUtils;
import utils.PropertiesUtils;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetParametersSupportedByCategoryTest {

    private String accessToken;
    private String uri;
    private String acceptHeader;
    private String parentCategoryId;
    private String parentCategoryParametersSize;
    private String parentCategoryParameterType;

    @BeforeClass
    public void setUp() {
        this.accessToken = AuthorizationUtils.getAccessTokenForApplication();
        this.uri = PropertiesUtils.getPropertyValue("api.uri.base")
                + PropertiesUtils.getPropertyValue("api.uri.parameters_endpoint");
        this.acceptHeader = PropertiesUtils.getPropertyValue("api.headers.accept");
        this.parentCategoryId = PropertiesUtils.getPropertyValue("test.data.parent_category_id");
        this.parentCategoryParametersSize = PropertiesUtils.getPropertyValue("test.data.parent_category_parameters_size");
        this.parentCategoryParameterType = PropertiesUtils.getPropertyValue("test.data.parent_category_parameters_type");
    }

    @Test
    public void categoryParameters_getParametersByCategoryId_returnsStatus200() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .statusCode(200);
    }

    @Test
    public void categoryParameters_getParametersByCategoryId_hasCorrectSize() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("parameters", hasSize(Integer.parseInt(parentCategoryParametersSize)));
    }

    @Test
    public void categoryParameters_getParametersByCategoryId_hasCorrectType() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("parameters.type", hasItem(parentCategoryParameterType));
    }

    @Test
    public void categoryParameters_getParametersByCategoryId_hasName() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("parameters.name", not(hasItem(emptyOrNullString())));
    }

    @Test
    public void categoryParameters_getParametersByCategoryId_hasOptions() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("parameters.options", hasSize(greaterThan(0)));
    }

    @Test
    public void categoryParameters_getParametersByCategoryId_hasUnitField() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("parameters.unit", not(empty()));
    }

    @Test
    public void categoryParameters_getParametersByCategoryId_hasRestrictions() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("parameters.restrictions", not(nullValue()));
    }

    @Test
    public void categoryParameters_getParametersByRandomCategoryId_returnsStatus404() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", UUID.randomUUID().toString())
                .when().get(uri)
                .then()
                .statusCode(404);
    }

    @Test
    public void categoryParameters_getParametersByRandomCategoryId_returnsError() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", UUID.randomUUID().toString())
                .when().get(uri)
                .then()
                .body("errors", hasSize(greaterThan(0)));
    }

    @Test
    public void categoryParameters_getParametersByRandomCategoryId_returnsErrorWithCorrectFields() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", UUID.randomUUID().toString())
                .when().get(uri)
                .then()
                .body("errors.code", not(empty()))
                .body("errors.message", not(empty()))
                .body("errors.details", not(empty()))
                .body("errors.path", not(empty()))
                .body("errors.userMessage", not(empty()));
    }
}
