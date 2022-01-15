package cc.ilooli.session;

import cc.ilooli.config.Configuration;

/**
 * 默认sql会话工厂
 * @author Merlin
 * @date 2021/12/27
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {this.configuration = configuration;}

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
