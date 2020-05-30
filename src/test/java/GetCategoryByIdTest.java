import org.hamcrest.MatcherAssert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AuthorizationUtils;
import utils.PropertiesUtils;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetCategoryByIdTest {

    private String accessToken;
    private String uri;
    private String acceptHeader;
    private String parentCategoryId;
    private String parentCategoryName;
    private String leafCategoryId;
    private String leafCategoryParentId;

    @BeforeClass
    public void setUp() {
        this.accessToken = AuthorizationUtils.getAccessTokenForApplication();
        this.uri = PropertiesUtils.getPropertyValue("api.uri.base")
                + PropertiesUtils.getPropertyValue("api.uri.category_endpoint") + "/{id}";
        this.acceptHeader = PropertiesUtils.getPropertyValue("api.headers.accept");
        this.parentCategoryId = PropertiesUtils.getPropertyValue("test.data.parent_category_id");
        this.parentCategoryName = PropertiesUtils.getPropertyValue("test.data.parent_category_name");
        this.leafCategoryId = PropertiesUtils.getPropertyValue("test.data.leaf_category_id");
        this.leafCategoryParentId = PropertiesUtils.getPropertyValue("test.data.leaf_category_parent_id");
    }

    @Test
    public void categoryDetails_getCategoryById_returnsStatus200() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .statusCode(200);
    }

    @Test
    public void categoryDetails_getCategoryById_returnsObjectWithCorrectFields() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("id", not(empty()))
                .body("name", not(empty()))
                .body("parent", not(empty()))
                .body("leaf", not(empty()))
                .body("options", not(empty()));
    }

    @Test
    public void categoryDetails_getCategoryById_returnsCorrectCategoryId() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("id", is(parentCategoryId));
    }

    @Test
    public void categoryDetails_getCategoryById_returnsCorrectCategoryName() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", parentCategoryId)
                .when().get(uri)
                .then()
                .body("name", is(parentCategoryName));
    }

    @Test
    public void categoryDetails_getCategoryById_returnsBooleanOptions() {
        Map<String, Object> options = given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", leafCategoryId)
                .when().get(uri)
                .then()
                .extract().jsonPath()
                .getMap("options");
        for (Map.Entry<String, Object> option : options.entrySet()) {
            MatcherAssert.assertThat(option.getValue().getClass(), typeCompatibleWith(Boolean.class));
        }
    }

    @Test
    public void categoryDetails_getLeafCategoryById_categoryIsLeaf() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", leafCategoryId)
                .when().get(uri)
                .then()
                .body("leaf", is(true));
    }

    @Test
    public void categoryDetails_getLeafCategoryById_returnsCorrectParent() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", leafCategoryId)
                .when().get(uri)
                .then()
                .body("parent.id", is(leafCategoryParentId));
    }

    @Test
    public void categoryDetails_getCategoryByRandomId_returnsStatus404() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .pathParam("id", UUID.randomUUID().toString())
                .when().get(uri)
                .then()
                .statusCode(404);
    }

    @Test
    public void categoryDetails_getCategoryByRandomId_returnsErrorWithCorrectFields() {
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
