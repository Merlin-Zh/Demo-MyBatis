package cc.ilooli.executor;

import cc.ilooli.config.Configuration;
import cc.ilooli.config.MappedStatement;
import cc.ilooli.utils.GenericTokenParser;
import cc.ilooli.utils.ParameterMapping;
import cc.ilooli.utils.ParameterMappingTokenHandler;
import cc.ilooli.utils.TokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单SQL执行者
 * @author Merlin
 * @date 2021/12/27
 */
public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration config, String statementId, Object... params) throws Exception {
        // 1、注册驱动，获取链接
        Connection connection = config.getDataSource().getConnection();
        // 2、通过statementId获取SQL语句
        String sql = config.getMappedStatementMap().get(statementId).getSql();
        // 3、转换SQL语句为JDBC statement类型的语句
        BoundSql boundSql = this.getBoundSql(sql);
        // 4、获取预处理对象preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        // 5、填充参数
        if (params != null) {
            this.setParameters(config, statementId, boundSql, preparedStatement, params[0]);
        }
        // 6、执行SQL
        ResultSet resultSet = preparedStatement.executeQuery();
        // 7、封装返回结果集
        List<Object> list = getResultList(config, statementId, resultSet);

        return (List<E>) list;
    }

    private List<Object> getResultList(Configuration config, String statementId, ResultSet resultSet) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SQLException, IntrospectionException {
        String resultType = config.getMappedStatementMap().get(statementId).getResultType();
        Class<?> resultTypeClass = this.getClassType(resultType);
        List<Object> list = new ArrayList<>();
        while (resultSet.next()) {
            // 获取元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            Object o = resultTypeClass.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 获取字段名
                String columnName = metaData.getColumnName(i);
                // 获取字段值
                Object value = resultSet.getObject(i);
                // 反射，根据数据库表和实体的映射关系设置实体的属性
                PropertyDescriptor descriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = descriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            list.add(o);
        }
        return list;
    }

    private void setParameters(Configuration config, String statementId, BoundSql boundSql, PreparedStatement ps, Object o)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, SQLException {

        String parameterType = config.getMappedStatementMap().get(statementId).getParameterType();
        Class<?> parameterTypeClass = this.getClassType(parameterType);

        for (int i = 0; i < boundSql.getParameterMappingList().size(); i++) {
            String parameterName = boundSql.getParameterMappingList().get(i).getContent();
            // 反射获取参数
            Field field = parameterTypeClass.getDeclaredField(parameterName);
            // 设置暴力访问
            field.setAccessible(true);
            ps.setObject(i + 1, field.get(o));
        }
    }

    private Class<?> getClassType(String className) throws ClassNotFoundException {
        if (className == null) {
            return null;
        }
        return Class.forName(className);
    }

    /**
     * 获取绑定sql
     * 1、将#{}使用？进行代替
     * 2、解析出#{}中的参数进行存储
     * @param sql sql
     * @return {@code BoundSql}
     */
    private BoundSql getBoundSql(String sql) {
        // 标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        // 解析之后的sql
        String parseSql = genericTokenParser.parse(sql);
        // 解析#{}中的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        // 返回封装的BoundSql对象
        return new BoundSql(parseSql, parameterMappings);
    }
}
