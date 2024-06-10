package org.example.application.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.application.component.ServiceActuator;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author fenggz
 * @description
 * @datetime 2024/6/8 22:14
 */
@RestController
@RequestMapping("/dispatcher")
@Slf4j
public class DispatcherService {

    @Resource
    private ServiceActuator serviceActuator;

    @GetMapping("/{service}")
    public Object dispatch(@PathVariable(value = "service") String serviceName, @RequestParam Map<String,Object> params){

        return serviceActuator.execute(serviceName,params);
    }
}
