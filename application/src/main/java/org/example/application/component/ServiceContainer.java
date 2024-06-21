package org.example.application.component;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.application.defaultservice.DefaultDataServiceImpl;
import org.example.interfaces.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author fenggz
 * @description
 * @datetime 2024/6/8 22:22
 */
@Component
@Slf4j
public class ServiceContainer {

    private static IDataService defaultDataService;

    public static Set<String> getServiceNames() {
        return container.keySet();
    }

    @Autowired
    public void setDefaultDataService(final DefaultDataServiceImpl defaultDataService) {
        ServiceContainer.defaultDataService = defaultDataService;
    }

    /**
     * 容器
     */
    private static volatile Map<String,Object> container = new Hashtable<>();

    public static void registerService(String serviceName, Object service) {
        log.debug("Registering service {}",serviceName);
        container.put(serviceName, service);
    }

    public static void removeService(String serviceName) {
        container.remove(serviceName);
    }

    public static Object getService(String serviceName) {
        Object dataService = container.get(serviceName);
        if (dataService == null) {
            dataService = defaultDataService;
        }
        return dataService;
    }
}
