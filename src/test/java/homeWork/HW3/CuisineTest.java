package homeWork.HW3;

import lesson3.AbstractTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CuisineTest extends AbstractTest {

  @Test
  void FindClassificationForDish() {
    given()
            .queryParam("apiKey", getApiKey())
            .contentType("application/x-www-form-urlencoded")
            .formParam("title", "African Chicken Peanut Stew")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .statusCode(200)
            .assertThat().body("cuisine", equalTo("African"));

  }

  @Test
  void FindClassificationByIngredients() {
    given()
            .queryParam("apiKey", getApiKey())
            .contentType("application/x-www-form-urlencoded")
            .body("{\n"
                    + " \"date\": 1644881179,\n"
                    + " \"slot\": 1,\n"
                    + " \"position\": 0,\n"
                    + " \"type\": \"INGREDIENTS\",\n"
                    + " \"value\": {\n"
                    + " \"ingredients\": [\n"
                    + " {\n"
                    + " \"name\": \"cheddar\"\n"
                    + " }\n"
                    + " ]\n"
                    + " }\n"
                    + "}")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .statusCode(200)
            .assertThat().body("cuisine", equalTo("Italian"));
  }
  @Test
  void FindClassificationForNonExistentIngredient() {
    given()
            .queryParam("apiKey", getApiKey())
            .contentType("application/x-www-form-urlencoded")
            .formParam("ingredientList", "karambruru")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .statusCode(200);

  }

  @Test
  void FindClassificationForNonExistentDish() {
    given()
            .queryParam("apiKey", getApiKey())
            .contentType("application/x-www-form-urlencoded")
            .formParam("title", "ABRAKADABRA")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .statusCode(200);
  }

  @Test
  void FindClassificationAllFields() {
    given()
            .queryParam("apiKey", getApiKey())
            .contentType("application/x-www-form-urlencoded")
            .formParam("title", "Homemade Garlic and Basil French Fries")
            .formParam("ingredientList", "3 oz pork shoulder")
            .formParam("language", "de")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .statusCode(200)
            .assertThat().body("cuisines", equalTo("French"));
  }

}
