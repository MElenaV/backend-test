package homeWork.HW5;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductsTest {
  static ProductService productService;
  Product product = null;
  Faker faker = new Faker();
  int id;

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
  void getProductsTest() throws IOException {
    Response<ResponseBody> response = productService.getProducts()
            .execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
  }

  @Test
  void getProductsTestById() throws IOException {
    Response<Product> response  = productService.getProductById(id)
            .execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(response.body().getId(), CoreMatchers.is(id));
  }

  @Test
  void getProductsTestByNonExistentId() throws IOException {
    int nonExistentId = 99;
    Response<Product> response  = productService.getProductById(nonExistentId)
            .execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(false));
    assertThat(response.code(), CoreMatchers.is(404));
  }

}
