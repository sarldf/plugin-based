package org.example.application.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.application.constant.SystemConstant;
import org.example.application.util.ClassLoaderUtil;
import org.example.application.util.SpringUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author fenggz
 * @description
 * @datetime 2024/5/26 23:17
 */
@RestController
@RequestMapping("/sampleService")
@Slf4j
public class SampleService {

    @Resource
    private SpringUtil springUtil;

    @GetMapping("/hello")
    public String hello(@RequestParam String msg){
        return "Hello " + msg;
    }


    @GetMapping("/loadPlugin")
    public String loadPlugin(@RequestParam String jarFileName){
        String jarFullPath = SystemConstant.JAR_PATH + jarFileName;
        try {
            JarFile jarFile = new JarFile(new File(jarFullPath));
            Enumeration<JarEntry> entries =  jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.endsWith(".class")) {
                    continue;
                }
                String className = name.substring(0, name.length() - 6);
                className = className.replace("/",".");

                log.debug("loader {}", jarFullPath);
                ClassLoader classLoader = ClassLoaderUtil.getClassLoader(jarFullPath);
                Class cls = classLoader.loadClass(className);
                springUtil.registerBean(cls.getName(),cls);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "Success loaded";
    }
}
