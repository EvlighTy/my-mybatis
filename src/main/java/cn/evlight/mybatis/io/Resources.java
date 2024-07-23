package cn.evlight.mybatis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @Description:
 * @Author: evlight
 * @Date: 2024/7/22
 */
public class Resources {

    public static Reader getResourceAsReader(String resourcePath) {
        ClassLoader[] classLoaders = {ClassLoader.getSystemClassLoader(), Thread.currentThread().getContextClassLoader()};
        for (ClassLoader classLoader : classLoaders) {
            InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
            if (null != inputStream) {
                return new InputStreamReader(inputStream);
            }
        }
        throw new RuntimeException("could not find resource " + resourcePath);
    }

}
