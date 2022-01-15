package cc.ilooli.config;

import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置类
 * @author Merlin
 * @date 2021/12/26
 */
@Data
public class Configuration {

    /** 数据源 */
    private DataSource dataSource;
    /** mapper语句map */
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
}
