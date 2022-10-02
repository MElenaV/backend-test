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

import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteProductTest {
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

  @SneakyThrows
  @Test
  void deleteProduct() {
    Response<ResponseBody> response = productService.deleteProduct(id).execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
  }

  @SneakyThrows
  @Test
  void deleteProductByNonExistentId() {
    int nonExistentId = 99;
    Response<ResponseBody> response = productService.deleteProduct(nonExistentId).execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(false));
    assertThat(response.code(), CoreMatchers.is(500));
  }

}
