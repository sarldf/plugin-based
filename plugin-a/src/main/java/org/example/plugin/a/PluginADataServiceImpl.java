package org.example.plugin.a;

import org.example.annotation.PluginService;
import org.example.interfaces.IDataService;

import java.util.Map;

/**
 * @author fenggz
 * @description
 * @datetime 2024/6/8 23:26
 */
@PluginService("pluginADataService")
public class PluginADataServiceImpl implements IDataService {
    /**
     * 处理数据接口方法
     *
     * @param params
     */
    @Override
    public Object handle(Map<String, Object> params) {
        return "plugin-a data service handle method. parameters is " + params.toString();
    }
}
