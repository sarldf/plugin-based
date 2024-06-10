package org.example.application.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author fenggz
 * @description
 * @datetime 2024/5/26 23:30
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void registerBean(String beanName, Class cls) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cls);
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) this.applicationContext;
        configurableApplicationContext.getBeanFactory().registerSingleton(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    public Object getBean(String beanName) {
        return this.applicationContext.getBean(beanName);
    }
}
