package cc.ilooli.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 映射语句
 * @author Merlin
 * @date 2021/12/26
 */
@Data@AllArgsConstructor@NoArgsConstructor
public class MappedStatement {

    /** id */
    private String id;
    /** 结果类型 */
    private String resultType;
    /** 参数类型 */
    private String parameterType;
    /** sql */
    private String sql;
}
