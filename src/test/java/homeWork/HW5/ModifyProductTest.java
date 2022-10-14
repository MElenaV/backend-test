package homeWork.HW5;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.Products;
import lesson6.db.model.ProductsExample;
import lombok.SneakyThrows;
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

public class ModifyProductTest {

  static ProductService productService;
  Product product = null;
  Product modifyProduct = null;
  Faker faker = new Faker();
  Long id;
  String title;
  String categoryTitle;
  int price;

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
  void modifyTitleProductInFoodCategoryTest() throws IOException {
    modifyProduct = new Product()
            .withId(Math.toIntExact(id))
            .withTitle("Новый продукт")
            .withCategoryTitle(product.getCategoryTitle())
            .withPrice(product.getPrice());

    Response<Product> response = productService.modifyProduct(modifyProduct)
            .execute();
    title = response.body().getTitle();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(title, CoreMatchers.is(modifyProduct.getTitle()));

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    ProductsExample example = new ProductsExample();
    example.createCriteria().andIdEqualTo(id);

    List<Products> list = productsMapper.selectByExample(example);
    assertThat(modifyProduct.getTitle(), CoreMatchers.is(list.get(0).getTitle()));
  }

  @Test
  void modifyCategoryTitleProductInFoodCategoryTest() throws IOException {
    modifyProduct = new Product()
            .withId(Math.toIntExact(id))
            .withTitle(product.getTitle())
            .withCategoryTitle("Electronic")
            .withPrice(product.getPrice());

    Response<Product> response = productService.modifyProduct(modifyProduct)
            .execute();

    categoryTitle = response.body().getCategoryTitle();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(categoryTitle, CoreMatchers.is(modifyProduct.getCategoryTitle()));

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    ProductsExample example = new ProductsExample();
    example.createCriteria().andIdEqualTo(modifyProduct.getId().longValue());

    List<Products> list2 = productsMapper.selectByExample(example);
    Products modifyProduct2 = list2.get(0);
    modifyProduct2.setTitle("Food2");
    productsMapper.updateByPrimaryKey(modifyProduct2);
    session.commit();
  }

  @Test
  void modifyPriceProductInFoodCategoryTest() throws IOException {
    modifyProduct = new Product()
            .withId(Math.toIntExact(id))
            .withTitle(product.getTitle())
            .withCategoryTitle(product.getCategoryTitle())
            .withPrice(999);

    Response<Product> response = productService.modifyProduct(modifyProduct)
            .execute();

    price = response.body().getPrice();
    assertThat(response.isSuccessful(), CoreMatchers.is(true));
    assertThat(price, CoreMatchers.is(modifyProduct.getPrice()));

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    ProductsExample example = new ProductsExample();
    example.createCriteria().andIdEqualTo(id);

    List<Products> list = productsMapper.selectByExample(example);
    assertThat(modifyProduct.getPrice(), CoreMatchers.is(list.get(0).getPrice()));
  }

  @Test
  void modifyProductInFoodCategoryTest() throws IOException {
    modifyProduct = new Product()
            .withId(Math.toIntExact(id))
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

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    ProductsExample example = new ProductsExample();
    example.createCriteria().andIdEqualTo(id);

    List<Products> list = productsMapper.selectByExample(example);
    assertThat(modifyProduct.getPrice(), CoreMatchers.is(list.get(0).getPrice()));
    assertThat(modifyProduct.getTitle(), CoreMatchers.is(list.get(0).getTitle()));
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

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    ProductsExample example = new ProductsExample();
    example.createCriteria().andIdEqualTo(id);

    List<Products> list = productsMapper.selectByExample(example);
    assertThat(id, CoreMatchers.is(list.get(0).getId()));
  }


  @SneakyThrows
  @AfterEach
  void tearDown() {
  //  Response<ResponseBody> response = productService.deleteProduct(id).execute();
 //   assertThat(response.isSuccessful(), CoreMatchers.is(true));

    ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
    productsMapper.deleteByPrimaryKey(id);
  }

  @SneakyThrows
  @AfterAll
  static void tearDownAll() {
    session.close();
  }
}
