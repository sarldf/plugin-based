package org.example.application.init;

import lombok.extern.slf4j.Slf4j;
import org.example.annotation.PluginService;
import org.example.application.component.ServiceContainer;
import org.example.application.constant.SystemConstant;
import org.example.application.util.ClassLoaderUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author fenggz
 * @description
 * @datetime 2024/5/26 23:49
 */
@Component
@Slf4j
public class PluginRegister extends ApplicationObjectSupport implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Map<String,Class> pluginServices = getAllPluginService();

        for (Map.Entry<String,Class> pluginServiceEntry : pluginServices.entrySet()) {
            String pluginServiceName = pluginServiceEntry.getKey();
            Class<?> pluginServiceCls = pluginServiceEntry.getValue();
            //注册到spring容器
            /*BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(pluginServiceCls);
            BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
            registry.registerBeanDefinition(pluginServiceName, beanDefinition);*/

            //注册到自定义服务容器
            registerPluginService(pluginServiceName,pluginServiceCls);

        }

        //开启一个线程监听服务jar包目录,30秒扫一次
        new Thread(() -> {
            Set<String> currentServiceNames = ServiceContainer.getServiceNames();
            Map<String,Class> pluginServices1 = getAllPluginService();
            for (Map.Entry<String,Class> pluginServiceEntry : pluginServices1.entrySet()) {
                String pluginServiceName = pluginServiceEntry.getKey();
                Class<?> pluginServiceCls = pluginServiceEntry.getValue();
                if(!currentServiceNames.contains(pluginServiceName)) {
                    //注册到自定义服务容器
                    registerPluginService(pluginServiceName,pluginServiceCls);
                }
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    private void registerPluginService(String pluginServiceName, Class<?> pluginServiceCls) {
        Object serviceObject = null;
        try {
            serviceObject = pluginServiceCls.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ServiceContainer.registerService(pluginServiceName,serviceObject);
    }

    private Map<String,Class> getAllPluginService() {
        return getPluginServices(new String[]{});
    }

    private Map<String,Class> getPluginServices(String[] jarNames) {

        Map<String,Class> pluginServices = new HashMap<>(16);

        File[] files = null;
        if(jarNames != null && jarNames.length > 0) {
            files = new File[jarNames.length];
            int index = 0;
            for (String jarName : jarNames) {
                files[index++] = new File(SystemConstant.JAR_PATH + jarName);
            }
        }else{
            File file = new File(SystemConstant.JAR_PATH);
            //筛选出目标jar文件列表
            files = file.listFiles((dir, name) -> name.endsWith("jar"));
        }
        for (File jarF : files) {
            if (jarF.isFile()) {
                //解析jar包中的类名，加载，注册到服务容器
                JarFile jarFile = null;
                try {
                    jarFile = new JarFile(jarF);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Enumeration<JarEntry> entries =  jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String name = jarEntry.getName();
                    if (!name.endsWith(".class")) {
                        continue;
                    }
                    String className = name.substring(0, name.length() - 6);
                    className = className.replace("/", ".");
                    String jarFullPath = SystemConstant.JAR_PATH + jarF.getName();
                    log.debug("loader {}", jarFullPath);
                    ClassLoader classLoader = ClassLoaderUtil.getClassLoader(jarFullPath);
                    Class cls = null, pluginServiceAnnotationCls = null;
                    try {
                        cls = classLoader.loadClass(className);
                        pluginServiceAnnotationCls = classLoader.loadClass(PluginService.class.getName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    if (cls.isAnnotationPresent(pluginServiceAnnotationCls)) {
                        PluginService pluginServiceAnnotation = (PluginService) AnnotationUtils.findAnnotation(cls, pluginServiceAnnotationCls);
                        String pluginServiceName = getPluginServiceName(pluginServiceAnnotation, jarFile);
                        pluginServices.put(pluginServiceName, cls);
                    }

                }
            }
        }
        return pluginServices;
    }

    private static String getPluginServiceName(PluginService pluginServiceAnnotation, JarFile jarFile) {
        String pluginServiceName = null;
        if(pluginServiceAnnotation != null){
            pluginServiceName = pluginServiceAnnotation.value();
        }else{
            Manifest manifest = null;
            try {
                manifest = jarFile.getManifest();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Attributes attributes = manifest.getMainAttributes();
            pluginServiceName = attributes.getValue("Service-Name");
        }
        return pluginServiceName;
    }

}
