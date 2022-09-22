package homeWork.HW3;

import io.restassured.path.json.JsonPath;
import lesson3.AbstractTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ComplexSearch extends AbstractTest {

  @Test
  void getSearchBurger() {

    JsonPath response = given()
            .queryParam("apiKey", getApiKey())
            .queryParam("query", "burger")
            .when()
            .get(getBaseUrl()+"recipes/complexSearch")
            .body()
            .jsonPath();
    assertThat(response.prettyPrint().contains("Burger"), is(true));

  }

  @Test
  void getRecipeWithNutritionalValue() {

    JsonPath response = given()
            .queryParam("apiKey", getApiKey())
            .queryParam("addRecipeNutrition", true)
            .when()
            .get(getBaseUrl()+"recipes/complexSearch")
            .body()
            .jsonPath();
    assertThat(response.prettyPrint().contains("nutrition"), is(true));

  }

  @Test
  void getTheFastestRecipe() {

    JsonPath response = given()
            .queryParam("apiKey", getApiKey())
            .queryParam("addRecipeInformation", "true")
            .queryParam("sort", "time")
            .queryParam("sortDirection", "asc")
            .queryParam("number", 1)
            .when()
            .get(getBaseUrl() + "recipes/complexSearch")
            .body()
            .jsonPath();
    assertThat(response.get("results[0].readyInMinutes"), equalTo(2));
  }

  @Test
  void getVeganRecipes() {

    JsonPath response = given()
            .queryParam("apiKey", getApiKey())
            .queryParam("addRecipeInformation", "true")
            .queryParam("diet", "vegetarian")
            .when()
            .get(getBaseUrl() + "recipes/complexSearch")
            .body()
            .jsonPath();

   int size = response.get("results.size()");
    int index;
    for (index = 0; index < size; index++) {
      assertThat(response.get("results["+index+"].vegetarian"), equalTo(true));
    }
  }

  @Test
  void GetRecipesFromSpecificAuthor() {
    JsonPath response = given()
            .queryParam("apiKey", getApiKey())
            .queryParam("author", "coffeebean")
            .queryParam("addRecipeInformation", "true")
            .when()
            .get(getBaseUrl() + "recipes/complexSearch")
            .body()
            .jsonPath();
    int size = response.get("results.size()");
    int index;
    for (index = 0; index < size; index++) {
      assertThat(response.get("results["+index+"].author"), equalTo("coffeebean"));
    }

  }

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
