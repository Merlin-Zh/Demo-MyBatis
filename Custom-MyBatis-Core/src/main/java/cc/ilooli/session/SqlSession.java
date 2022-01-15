package cc.ilooli.session;

import java.sql.SQLException;
import java.util.List;

/**
 * sql会话
 * @author Merlin
 * @date 2021/12/27
 */
public interface SqlSession {

    /**
     * 查询多个，列表返回
     * @param statementId 语句id
     * @param params      参数个数
     * @return {@code List<E>}
     *
     * @throws Exception 异常
     */
    <E> List<E> selectList(String statementId, Object... params) throws Exception;

    /**
     * 选择列表
     * @param statementId 语句id
     * @return {@code List<E>}
     *
     * @throws Exception 异常
     */
    default <E> List<E> selectList(String statementId) throws Exception{
        return this.selectList(statementId, (Object) null);
    }

    /**
     * 查询一个
     * @param statementId 语句id
     * @param params      参数个数
     * @return {@code T}
     *
     * @throws Exception 异常
     */
    <T> T selectOne(String statementId, Object... params) throws Exception;

    /**
     * 插入
     * @param target 目标
     * @return {@code Boolean}
     */
    Boolean insert(Object target);

    /**
     * 更新
     * @param target 目标
     * @return boolean
     */
    boolean update(Object target);

    /**
     * 删除
     * @param target 目标
     * @return boolean
     */
    boolean delete(Object target);
}
