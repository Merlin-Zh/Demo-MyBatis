package cc.ilooli.executor;

import cc.ilooli.utils.ParameterMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定sql
 * @author Merlin
 * @date 2021/12/27
 */
@Data@AllArgsConstructor
public class BoundSql {

    /** sql文本 */
    private String sqlText;

    /** 参数映射列表 */
    private List<ParameterMapping> parameterMappingList = new ArrayList<>();
}
