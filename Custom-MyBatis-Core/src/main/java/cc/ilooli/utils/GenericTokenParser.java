package cc.ilooli.utils;

/**
 * 通用标记解析器
 * @author Merlin
 * @date 2021/12/27
 */
public class GenericTokenParser {
    private final String openToken;
    private final String closeToken;
    private final TokenHandler handler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }

    /**
     * 解析${}和#{}
     * 该方法主要实现了配置文件、脚本等片段中占位符的解析、处理工作，并返回最终需要的数据
     * 其中，解析工作由此方法完成，处理工作有处理器handler的handleToken()方法完成
     * @param text 文本
     * @return {@code String}
     */
    public String parse(String text) {
        // 验证参数，如果为null则返回空字符串
        if (text == null || text.isEmpty()) {
            return "";
        }

        // 验证是否包含开始标签，如果不包含，默认其不是占位符不处理，否则执行
        int start = text.indexOf(openToken);
        if (start == -1) {
            return text;
        }

        // 把text转成字符数组src，并定义默认偏移量offset为0，存储最终需要返回的字符串变量builder
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;

        while (start > -1) {
            // 判断开始标记前是否存在转义字符，有则不做处理，否则继续
            if (start > 0 && src[start - 1] == '\\') {
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // 重置expression，避免空指针或老数据干扰
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }

                builder.append(src, offset, start - offset);
                offset = start + openToken.length();

                // 结束标记位置
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    // 若结束标记前存在转义字符则进行偏移寻找下一个，否则继续
                    if (end > offset && src[end - 1] == '\\') {
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        // offset = end + closeToken.length();
                        break;
                    }
                }

                // 未找到结束标记
                if (end == -1) {
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    // 首先根据参数的key（即expression）进行参数处理，返回？作为占位符
                    builder.append(handler.handleToken(expression.toString()));
                    offset = end + closeToken.length();
                }
            }

            start = text.indexOf(openToken, offset);
        }

        if (offset<src.length){
            builder.append(src,offset,src.length - offset);
        }
        return builder.toString();
    }
}
