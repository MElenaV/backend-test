package homeWork.HW4;

import lesson4.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CuisineTest extends AbstractTest {

  @Test
  void FindClassificationForDish() {
    Response response = given(requestSpecification)
            .contentType("application/x-www-form-urlencoded")
            .formParam("title", "African Chicken Peanut Stew")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .extract()
            .response()
            .body()
            .as(Response.class);
            assertThat(response.getCuisine(), equalTo("African"));
  }

  @Test
  void FindClassificationByIngredients() {
    Response response = given(requestSpecification)
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
            .extract()
            .response()
            .body()
            .as(Response.class);
            assertThat(response.getCuisine(), equalTo("Mediterranean"));
  }

  @Test
  void FindClassificationForNonExistentIngredient() {
    given(requestSpecification)
            .contentType("application/x-www-form-urlencoded")
            .formParam("ingredientList", "karambruru")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .spec(responseSpecification);
  }

  @Test
  void FindClassificationForNonExistentDish() {
    given(requestSpecification)
            .contentType("application/x-www-form-urlencoded")
            .formParam("title", "ABRAKADABRA")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .spec(responseSpecification);
  }

  @Test
  void FindClassificationAllFields() {
    given(requestSpecification)
            .contentType("application/x-www-form-urlencoded")
            .formParam("title", "Homemade Garlic and Basil French Fries")
            .formParam("ingredientList", "3 oz pork shoulder")
            .formParam("language", "de")
            .when()
            .post(getBaseUrl() + "recipes/cuisine")
            .then()
            .spec(responseSpecification);
  }

}
