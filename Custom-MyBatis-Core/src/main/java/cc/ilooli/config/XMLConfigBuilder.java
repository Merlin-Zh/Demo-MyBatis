package cc.ilooli.config;

import cc.ilooli.io.Resources;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * xml config建设者
 * @author Merlin
 * @date 2021/12/26
 */
@SuppressWarnings({"AlibabaClassNamingShouldBeCamel", "AlibabaLowerCamelCaseVariableNaming"})
public class XMLConfigBuilder {

    private final Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 使用dom4j解析配置文件，封装configuration
     * @param is 字节输入流
     * @return {@code Configuration}
     */
    public Configuration parse(InputStream is) throws DocumentException, PropertyVetoException {
        Element root = new SAXReader().read(is).getRootElement();
        // 解析xml文件，获取Properties
        Properties properties = this.getPropertiesFromXML(root);
        // 通过Properties获取数据源
        ComboPooledDataSource dataSource = this.getDataSource(properties);
        // 将数据源封装进configuration
        this.configuration.setDataSource(dataSource);
        // mapper.xml解析
        this.getMappedStatement(root);

        return this.configuration;
    }

    private Properties getPropertiesFromXML(Element root) throws DocumentException {
        // 使用dom4j读取xml文件
        // Document document = new SAXReader().read(is);
        // 获取配置文件的根节点
        // Element root = document.getRootElement();
        // 获取dataSource节点
        Element dataSource = root.element("dataSource");
        // 获取配置文件中的property节点
        List<Element> elements = dataSource.elements("property");
        // 创建一个Properties对象来存储name和value
        Properties properties = new Properties();
        elements.forEach(element -> {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        });

        return properties;
    }

    private ComboPooledDataSource getDataSource(Properties properties) throws PropertyVetoException {
        // 创建C3P0数据源
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        // 设置各属性
        dataSource.setDriverClass(properties.getProperty("driverClass"));
        dataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        dataSource.setUser(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        return dataSource;
    }

    private void getMappedStatement(Element root) throws DocumentException {
        // Document document = new SAXReader().read(is);
        // Element root = document.getRootElement();
        // 获取mapper节点
        List<Element> mapperPaths = root.elements("mapper");
        for (Element element : mapperPaths) {
            // 获取resource，即mapper路径
            String mapperPath = element.attributeValue("resource");
            InputStream stream = Resources.getResourcesAsStream(mapperPath);
            // 将configuration传入XMLMapperBuilder，进行mapper解析后封装到其中的mappedStatementMap
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(this.configuration);
            // 将字节流传入xmlMapperBuilder的parse方法进行解析
            xmlMapperBuilder.parse(stream);
        }
    }
}
