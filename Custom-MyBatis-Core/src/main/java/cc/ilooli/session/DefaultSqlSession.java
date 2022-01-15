package cc.ilooli.session;

import cc.ilooli.config.Configuration;
import cc.ilooli.executor.SimpleExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * 默认sql会话
 * @author Merlin
 * @date 2021/12/27
 */
public class DefaultSqlSession implements SqlSession {

    private final Logger logger = LoggerFactory.getLogger(DefaultSqlSession.class);

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {this.configuration = configuration;}

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
        // 调用SimpleExecutor的query方法执行查询
        return new SimpleExecutor().query(this.configuration, statementId, params);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<T> list = this.selectList(statementId, params);
        if (list == null || list.isEmpty()) {
            logger.warn("未获取到结果");
            return null;
        }else if (list.size() > 1){
            logger.warn("获取到的结果过多");
            return null;
        }

        return list.get(0);
    }

    @Override
    public Boolean insert(Object target) {
        return null;
    }

    @Override
    public boolean update(Object target) {
        return false;
    }

    @Override
    public boolean delete(Object target) {
        return false;
    }
}
