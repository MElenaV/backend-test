package homeWork.HW4;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ShoppingListTest extends AbstractTest {


  String functionAddToShoppingList() {
    String addedId = given()
            .queryParam("hash", "86e9dfbecd65b7b944f7d9709487139586d31de7")
            .queryParam("apiKey", getApiKey())
            .body("{\n"
                    + " \"item\": 1 package baking powder,\n"
                    + " \"aisle\": Baking,\n"
                    + " \"parse\": true,\n"
                    + "}")
            .when()
            .post(getBaseUrl()+"mealplanner/" + getUsername() + "/shopping-list/items")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .get("id")
            .toString();
    return addedId;
  }

  @Test
  void addToShoppingList() {
    JsonPath response = given()
            .queryParam("hash", "86e9dfbecd65b7b944f7d9709487139586d31de7")
            .queryParam("apiKey", getApiKey())
            .body("{\n"
                    + " \"item\": 1 package baking powder,\n"
                    + " \"aisle\": Baking,\n"
                    + " \"parse\": true,\n"
                    + "}")
            .when()
            .post(getBaseUrl()+"mealplanner/" + getUsername() + "/shopping-list/items")
            .jsonPath();
    assertThat(response.prettyPrint().contains("id"), is(true));
  }

  @Test
  void deleteFromShoppingList () {
    String id = functionAddToShoppingList();
    given()
            .queryParam("hash", "86e9dfbecd65b7b944f7d9709487139586d31de7")
            .queryParam("apiKey", getApiKey())
            .delete(getBaseUrl()+"mealplanner/" + getUsername() + "/shopping-list/items/" + id)
            .then()
            .statusCode(200);
  }

  @Test
  void getShoppingList () {
    JsonPath response = given(requestSpecification)
            .when()
            .get(getBaseUrl() + "mealplanner/" + getUsername() + "/shopping-list")
            .then()
            .statusCode(200)
            .extract().jsonPath();
    assertThat(response.prettyPrint().contains("aisles"), is(true));
  }

}
