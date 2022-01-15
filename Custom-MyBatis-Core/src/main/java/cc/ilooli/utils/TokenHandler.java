package cc.ilooli.utils;

/**
 * 标记处理程序
 * @author Merlin
 * @date 2021/12/27
 */
public interface TokenHandler {
    /**
     * 处理标记
     * @param content 内容
     * @return {@code char[]}
     */
    String handleToken(String content);
}
