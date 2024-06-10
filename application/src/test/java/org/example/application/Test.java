package org.example.application;

import org.example.annotation.PluginService;
import org.example.application.defaultservice.DefaultDataServiceImpl;

/**
 * @author fenggz
 * @description
 * @datetime 2024/6/10 0:48
 */
@PluginService(value="hah")
public class Test {

    public static void main(String[] args) {
        Class<?> clazz = DefaultDataServiceImpl.class;
        PluginService pluginService = clazz.getAnnotation(PluginService.class);
        System.out.println(pluginService.value());
    }
}
