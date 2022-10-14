package homeWork.HW5;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.Products;
import lesson6.db.model.ProductsExample;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductsTest {
  static ProductService productService;
  Product product = null;
  Faker faker = new Faker();
  Long id;

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
    id =  response.body().getId().longValue();
  }

  @Test
  void getProductsTest() throws IOException {
    Response<ResponseBody> response = productService.getProducts()
            .execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
  }

  @Test
  void getProductsTestById() throws IOException {
    Response<Product> response  = productService.getProductById(Math.toIntExact(id))
            .execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(response.body().getId(), CoreMatchers.is(Math.toIntExact(id)));

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    ProductsExample example = new ProductsExample();
    example.createCriteria().andIdEqualTo(id);

    List<Products> list = productsMapper.selectByExample(example);
    assertThat(id, CoreMatchers.is(list.get(0).getId()));
  }

  @Test
  void getProductsTestByNonExistentId() throws IOException {
    int nonExistentId = 9999;
    Response<Product> response  = productService.getProductById(nonExistentId)
            .execute();
    assertThat(response.isSuccessful(), CoreMatchers.is(false));
    assertThat(response.code(), CoreMatchers.is(404));

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    ProductsExample example = new ProductsExample();
    example.createCriteria().andIdEqualTo(id);

    List<Products> list = productsMapper.selectByExample(example);
    assertThat(id, CoreMatchers.is(list.get(0).getId()));
  }

  @SneakyThrows
  @AfterEach
  void tearDown() {
    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    productsMapper.deleteByPrimaryKey(id);
    session.commit();
  }

  @SneakyThrows
  @AfterAll
  static void tearDownAll() {
    session.close();
  }

}
