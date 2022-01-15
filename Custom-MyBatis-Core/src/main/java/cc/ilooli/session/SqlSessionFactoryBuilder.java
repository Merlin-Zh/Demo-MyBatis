package cc.ilooli.session;

import cc.ilooli.config.Configuration;
import cc.ilooli.config.XMLConfigBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * sql会话工厂建造者
 * @author Merlin
 * @date 2021/12/26
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream is) throws DocumentException, PropertyVetoException {
        // 1、使用dom4j解析配置文件封装到configuration
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse(is);

        // 2、创建SqlSessionFactory对象，工厂类：生产sqlSession会话对象

        return new DefaultSqlSessionFactory(configuration);

    }
}
