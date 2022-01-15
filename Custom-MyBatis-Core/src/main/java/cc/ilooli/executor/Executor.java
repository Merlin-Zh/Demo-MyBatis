package cc.ilooli.executor;

import cc.ilooli.config.Configuration;
import cc.ilooli.config.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * SQL执行器
 * @author Merlin
 * @date 2021/12/27
 */
public interface Executor {

    /**
     * 查询
     * @param config      配置
     * @param params      参数
     * @param statementId 语句id
     * @return {@code List<E>}
     */
    <E> List<E> query(Configuration config, String statementId, Object... params) throws Exception;
}
