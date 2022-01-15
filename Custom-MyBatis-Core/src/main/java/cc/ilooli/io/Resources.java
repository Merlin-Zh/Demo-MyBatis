package cc.ilooli.io;

import java.io.InputStream;

/**
 * @author Merlin
 */
public class Resources {

    /**
     * 根据配置文件的路径将配置文件加载成字节流到内存中
     * @param path 配置路径
     * @return {@code InputStream}
     */
    public static InputStream getResourcesAsStream(String path){
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
