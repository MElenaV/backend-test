package homeWork.HW3;

import io.restassured.path.json.JsonPath;
import lesson3.AbstractTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ComplexSearchTest extends AbstractTest {

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
}
