package org.example.application.component;

import lombok.extern.slf4j.Slf4j;
import org.example.interfaces.IDataService;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author fenggz
 * @description 服务执行器
 * @datetime 2024/6/8 22:18
 */
@Slf4j
@Component
public class ServiceActuator {


    public Object execute(String serviceName, Map<String,Object> params){
        Object serviceInstance = ServiceContainer.getService(serviceName);
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = Map.class;
        try {
            Method method = serviceInstance.getClass().getDeclaredMethod("handle",parameterTypes);
            method.setAccessible(true);
            return method.invoke(serviceInstance,params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
