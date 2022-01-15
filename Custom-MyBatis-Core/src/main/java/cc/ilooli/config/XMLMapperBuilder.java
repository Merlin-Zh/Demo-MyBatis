package cc.ilooli.config;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * xml mapper建设者
 * @author Merlin
 * @date 2021/12/26
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class XMLMapperBuilder {

    private final Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream is) throws DocumentException {
        Element root = new SAXReader().read(is).getRootElement();
        // 获取mapper.xml中的节点信息并封装为MappedStatement存进configuration
        this.getSelectMappedStatement(root);
        this.getInsertMappedStatement(root);
        this.getUpdateMappedStatement(root);
        this.getDeleteMappedStatement(root);
    }

    private void getSelectMappedStatement(Element root) {
        // 获取select节点
        List<Element> selectList = root.elements("select");
        this.getMappedStatement(root, selectList);
    }

    private void getInsertMappedStatement(Element root) {
        // 获取insert节点
        List<Element> insertList = root.elements("insert");
        this.getMappedStatement(root, insertList);
    }

    private void getUpdateMappedStatement(Element root) {
        // 获取update节点
        List<Element> insertList = root.elements("update");
        this.getMappedStatement(root, insertList);
    }

    private void getDeleteMappedStatement(Element root) {
        // 获取delete节点
        List<Element> insertList = root.elements("delete");
        this.getMappedStatement(root, insertList);
    }

    private void getMappedStatement(Element root, List<Element> list) {
        list.forEach(element -> {
            // 获取各个属性
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");
            String sql = element.getTextTrim();
            // 封装MappedStatement
            MappedStatement ms = new MappedStatement();
            ms.setId(id);
            ms.setParameterType(parameterType);
            ms.setResultType(resultType);
            ms.setSql(sql);
            // 将MappedStatement存进configuration
            // key: statementId-->namespace+"."+id
            this.configuration.getMappedStatementMap().put(
                    root.attributeValue("namespace") + "." + id, ms);
        });
    }
}
