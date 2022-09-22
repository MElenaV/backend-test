package homeWork.HW3;

import lesson3.AbstractTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class MealPlanningTest extends AbstractTest {

  @Test
  void addMealTest() {
    String id = given()
            .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
            .queryParam("apiKey", getApiKey())
            .body("{\n"
                    + " \"date\": 1644881179,\n"
                    + " \"slot\": 1,\n"
                    + " \"position\": 0,\n"
                    + " \"type\": \"INGREDIENTS\",\n"
                    + " \"value\": {\n"
                    + " \"ingredients\": [\n"
                    + " {\n"
                    + " \"name\": \"1 banana\"\n"
                    + " },\n"
                    + " {\n"
                    + " \"name\": \"coffee\",\n"
                    + " \"unit\": \"cup\"\n"
                    + " },\n"
                    + " ]\n"
                    + " }\n"
                    + "}")
            .when()
            .post("https://api.spoonacular.com/mealplanner/geekbrains/items")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .get("id")
            .toString();

    given()
            .queryParam("hash", "a3da66460bfb7e62ea1c96cfa0b7a634a346ccbf")
            .queryParam("apiKey", getApiKey())
            .delete("https://api.spoonacular.com/mealplanner/geekbrains/items/" + id)
            .then()
            .statusCode(200);
  }
}
