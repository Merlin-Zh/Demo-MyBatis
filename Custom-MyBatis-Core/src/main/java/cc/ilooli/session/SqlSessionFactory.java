package cc.ilooli.session;

import cc.ilooli.config.Configuration;

/**
 * sql会话工厂
 * @author Merlin
 * @date 2021/12/26
 */
public interface SqlSessionFactory {
    /**
     * 打开会话
     * @return {@code SqlSession}
     */
    SqlSession openSession();
}
