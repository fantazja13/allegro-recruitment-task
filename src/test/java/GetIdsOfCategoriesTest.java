import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.AuthorizationUtils;
import utils.PropertiesUtils;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class GetIdsOfCategoriesTest {

    private String accessToken;
    private String uri;
    private String acceptHeader;
    private String mainCategoriesSize;
    private String parentCategoryId;
    private String leafCategoryId;

    @BeforeClass
    public void setUp() {
        this.accessToken = AuthorizationUtils.getAccessTokenForApplication();
        this.uri = PropertiesUtils.getPropertyValue("api.uri.base") + PropertiesUtils.getPropertyValue("api.uri.category_endpoint");
        this.acceptHeader = PropertiesUtils.getPropertyValue("api.headers.accept");
        this.mainCategoriesSize = PropertiesUtils.getPropertyValue("test.data.main_categories_size");
        this.parentCategoryId = PropertiesUtils.getPropertyValue("test.data.parent_category_id");
        this.leafCategoryId = PropertiesUtils.getPropertyValue("test.data.leaf_category_id");
    }

    @Test
    public void mainCategoriesList_getAllCategories_returnsStatus200() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .when().get(uri)
                .then()
                .statusCode(200);
    }

    @Test
    public void mainCategoriesList_getAllCategories_returnsObjectWithCorrectFields() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .when().get(uri)
                .then()
                .body("categories.id", not(empty()))
                .body("categories.name", not(empty()))
                .body("categories.parent", not(empty()))
                .body("categories.leaf", not(empty()))
                .body("categories.options", not(empty()));
    }

    @Test
    public void mainCategoriesList_getAllCategories_hasCorrectSize() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .when().get(uri)
                .then()
                .body("categories", hasSize(Integer.parseInt(mainCategoriesSize)));

    }

    @Test
    public void mainCategoriesList_getAllCategories_allMainCategoriesHaveNames() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .when().get(uri)
                .then()
                .body("categories.name", not(hasItem(emptyOrNullString())));
    }

    @Test
    public void categoriesList_getChildrenCategories_returnsStatus200() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .queryParam("parent.id", parentCategoryId)
                .when().get(uri)
                .then()
                .statusCode(200);
    }

    @Test
    public void categoriesList_getChildrenCategories_returnsCategoriesWithCorrectParent() {
        List<String> parents = given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .queryParam("parent.id", parentCategoryId)
                .when().get(uri)
                .then()
                .extract()
                .jsonPath().getList("categories.parent.id");
        for (String parent : parents) {
            assertEquals(parent, parentCategoryId);
        }
    }

    @Test
    public void categoriesList_getChildrenCategoriesForLeafCategory_returnsEmptyArray() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .queryParam("parent.id", leafCategoryId)
                .when().get(uri)
                .then()
                .body("categories", empty());
    }

    @Test
    public void categoriesList_getChildrenCategoriesByRandomId_returnsStatus404() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .queryParam("parent.id", UUID.randomUUID().toString())
                .when().get(uri)
                .then()
                .statusCode(404);
    }

    @Test
    public void categoriesList_getChildrenCategoriesByRandomId_returnsErrorWithCorrectFields() {
        given()
                .auth().oauth2(accessToken)
                .accept(acceptHeader)
                .queryParam("parent.id", UUID.randomUUID().toString())
                .when().get(uri)
                .then()
                .body("errors.code", not(empty()))
                .body("errors.message", not(empty()))
                .body("errors.details", not(empty()))
                .body("errors.path", not(empty()))
                .body("errors.userMessage", not(empty()));
    }
}
