package org.example.application.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author fenggz
 * @description
 * @datetime 2024/5/27 0:01
 */
public class ClassLoaderUtil {

    public static ClassLoader getClassLoader(String jarPath) {

        URLClassLoader urlClassLoader = null;
        try {
            urlClassLoader = new URLClassLoader(new URL[]{new File(jarPath).toURI().toURL()}, Thread.currentThread().getContextClassLoader());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return urlClassLoader;
    }
}
