import cc.ilooli.io.Resources;
import cc.ilooli.pojo.User;
import cc.ilooli.session.SqlSession;
import cc.ilooli.session.SqlSessionFactory;
import cc.ilooli.session.SqlSessionFactoryBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

public class DemoTest {

    @SneakyThrows
    @Test
    void test() {
        InputStream stream = Resources.getResourcesAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User user = new User();
        user.setId(1);
        user.setName("merlin");
        // Object o = sqlSession.selectOne("cc.ilooli.mapper.findOne", user);
        // User o = sqlSession.selectOne("cc.ilooli.mapper.findOne", user);
        // System.out.println("o = " + o);
        List<Object> list = sqlSession.selectList("cc.ilooli.mapper.findAll");
        list.forEach(System.out::println);
    }
}
