package cc.ilooli.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数映射标记处理程序
 * @author Merlin
 * @date 2021/12/27
 */
@Data
public class ParameterMappingTokenHandler implements TokenHandler{

    private final List<ParameterMapping> parameterMappings = new ArrayList<>();

    /**
     * 处理标记参数
     * @param content 参数名称：#{id}中的id
     * @return {@code String}
     */
    @Override
    public String handleToken(String content) {
        parameterMappings.add(this.buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content){
        return new ParameterMapping(content);
    }

}
