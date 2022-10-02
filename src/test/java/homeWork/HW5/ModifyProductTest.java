package homeWork.HW5;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class ModifyProductTest {

  static ProductService productService;
  Product product = null;
  Product modifyProduct = null;
  Faker faker = new Faker();
  int id;
  String title;
  String categoryTitle;
  int price;

  @BeforeAll
  static void beforeAll() {
    productService = RetrofitUtils.getRetrofit()
            .create(ProductService.class);
  }

  @SneakyThrows
  @BeforeEach
  void setUp() {
    product = new Product()
            .withTitle(faker.food().ingredient())
            .withCategoryTitle("Food")
            .withPrice((int) (Math.random() * 10000));
    Response<Product> response = productService.createProduct(product)
            .execute();
    id =  response.body().getId();
  }

  @Test
  void modifyTitleProductInFoodCategoryTest() throws IOException {
    modifyProduct = new Product()
            .withId(id)
            .withTitle("Новый продукт")
            .withCategoryTitle(product.getCategoryTitle())
            .withPrice(product.getPrice());

    Response<Product> response = productService.modifyProduct(modifyProduct)
            .execute();
    title = response.body().getTitle();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(title, CoreMatchers.is(modifyProduct.getTitle()));
  }

  @Test
  void modifyCategoryTitleProductInFoodCategoryTest() throws IOException {
    modifyProduct = new Product()
            .withId(id)
            .withTitle(product.getTitle())
            .withCategoryTitle("Electronic")
            .withPrice(product.getPrice());

    Response<Product> response = productService.modifyProduct(modifyProduct)
            .execute();

    categoryTitle = response.body().getCategoryTitle();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(categoryTitle, CoreMatchers.is(modifyProduct.getCategoryTitle()));
  }

  @Test
  void modifyPriceProductInFoodCategoryTest() throws IOException {
    modifyProduct = new Product()
            .withId(id)
            .withTitle(product.getTitle())
            .withCategoryTitle(product.getCategoryTitle())
            .withPrice(999);

    Response<Product> response = productService.modifyProduct(modifyProduct)
            .execute();

    price = response.body().getPrice();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(price, CoreMatchers.is(modifyProduct.getPrice()));
  }

  @Test
  void modifyProductInFoodCategoryTest() throws IOException {
    modifyProduct = new Product()
            .withId(id)
            .withTitle("Новый продукт")
            .withCategoryTitle("Electronic")
            .withPrice(999);

    Response<Product> response = productService.modifyProduct(modifyProduct)
            .execute();

    title = response.body().getTitle();
    categoryTitle = response.body().getCategoryTitle();
    price = response.body().getPrice();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(title, CoreMatchers.is(modifyProduct.getTitle()));
    assertThat(categoryTitle, CoreMatchers.is(modifyProduct.getCategoryTitle()));
    assertThat(price, CoreMatchers.is(modifyProduct.getPrice()));
  }

  @Test
  void modifyProductInFoodCategoryTestByNonExistentId() throws IOException {
    int nonExistentId = 99;
    modifyProduct = new Product()
            .withId(nonExistentId)
            .withTitle("Новый продукт")
            .withCategoryTitle("Electronic")
            .withPrice(999);

    Response<Product> response = productService.modifyProduct(modifyProduct)
            .execute();

    assertThat(response.isSuccessful(), CoreMatchers.is(false));
    assertThat(response.code(), CoreMatchers.is(400));
  }


  @SneakyThrows
  @AfterEach
  void tearDown() {
    Response<ResponseBody> response = productService.deleteProduct(id).execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
  }
}
