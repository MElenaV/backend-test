package homeWork.HW5;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.Products;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteProductTest {
  static ProductService productService;
  Product product = null;
  Faker faker = new Faker();
  int id;

  static SqlSession session = null;
  static String resource = "mybatis-config.xml";
  static InputStream inputStream;

  @BeforeAll
  static void beforeAll() {
    productService = RetrofitUtils.getRetrofit()
            .create(ProductService.class);

    try {
      inputStream = Resources.getResourceAsStream(resource);
    } catch (IOException e) {
      e.printStackTrace();
    }
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    session = sqlSessionFactory.openSession();
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
    id = response.body().getId();
  }

  @SneakyThrows
  @Test
  void deleteProduct() {
    Response<ResponseBody> response = productService.deleteProduct(id).execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    List<Products> list = (List<Products>) productsMapper.selectByPrimaryKey(id);
    assertThat(CoreMatchers.is(list.get(0).getId()), null);
  }

  @SneakyThrows
  @Test
  void deleteProductByNonExistentId() {
    int nonExistentId = 99;
    Response<ResponseBody> response = productService.deleteProduct(nonExistentId).execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(false));
    assertThat(response.code(), CoreMatchers.is(500));

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    List<Products> list = (List<Products>) productsMapper.selectByPrimaryKey(id);
    assertThat(id, CoreMatchers.is(list.get(0).getId()));
    productsMapper.deleteByPrimaryKey(id);
    session.close();
  }

}
