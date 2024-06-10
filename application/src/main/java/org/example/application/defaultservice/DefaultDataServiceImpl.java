package org.example.application.defaultservice;

import org.example.annotation.PluginService;
import org.example.interfaces.IDataService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author fenggz
 * @description
 * @datetime 2024/6/8 23:21
 */
@PluginService("defaultDataService")
@Component
public class DefaultDataServiceImpl implements IDataService {
    /**
     * 处理数据接口方法
     *
     * @param params
     */
    @Override
    public Object handle(Map<String, Object> params) {
        return "default data service handle method. parameters is " + params.toString();
    }
}
