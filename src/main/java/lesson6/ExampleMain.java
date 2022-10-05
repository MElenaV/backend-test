package lesson6;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;



public class ExampleMain {

  public static void main( String[] args ) throws IOException {
    SqlSession session = null;
    try {
      String resource = "mybatis-config.xml";
      InputStream inputStream = Resources.getResourceAsStream(resource);
      SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
      session = sqlSessionFactory.openSession();
      lesson6.db.dao.CategoriesMapper categoriesMapper = session.getMapper(lesson6.db.dao.CategoriesMapper.class);
      lesson6.db.model.CategoriesExample example = new lesson6.db.model.CategoriesExample();

      example.createCriteria().andIdEqualTo(1);
      List<lesson6.db.model.Categories> list = categoriesMapper.selectByExample(example);
      System.out.println(categoriesMapper.countByExample(example));

      lesson6.db.model.Categories categories = new lesson6.db.model.Categories();
      categories.setTitle("test");
      categoriesMapper.insert(categories);
      session.commit();

      lesson6.db.model.CategoriesExample example2 = new lesson6.db.model.CategoriesExample();
      example2.createCriteria().andTitleLike("%test%");
      List<lesson6.db.model.Categories> list2 = categoriesMapper.selectByExample(example2);
      lesson6.db.model.Categories categories2 = list2.get(0);
      categories2.setTitle("test100");
      categoriesMapper.updateByPrimaryKey(categories2);
      session.commit();

      categoriesMapper.deleteByPrimaryKey(categories2.getId());
      session.commit();

    } finally {
      session.close();
    }


  }
}