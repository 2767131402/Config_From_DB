/**
 * BBD Service Inc
 * All Rights Reserved @2017
 */
package com.zhenglei.config;

import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * @author zhenglei
 */
@SuppressWarnings("all")
public class DiamondConfListener implements ApplicationListener, Ordered {
    private static final String CONFIG_PROPERTY_DATA_ID = "conf.data.id";
    private static final String CONFIG_PROPERTY_GROUP_ID = "conf.group.id";
    private static final String DIAMOND_PROPERTIES = "diamondProperties";
    /**
     * 超时时间
     */
    private final long TIMEOUT = 3000;
    /**
     * 配置属性
     */
    private volatile Properties properties = new Properties();
    /**
     * 缓存Environment实例
     */
    private volatile Environment environment = null;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            ApplicationEnvironmentPreparedEvent evt = (ApplicationEnvironmentPreparedEvent) event;

            String dataId = evt.getEnvironment().getProperty(CONFIG_PROPERTY_DATA_ID);
            String groupId = evt.getEnvironment().getProperty(CONFIG_PROPERTY_GROUP_ID);

            if (null != dataId && null != groupId) {
                if (environment == null) {
                    environment = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
                }
                init(groupId, dataId);
            }
        }
    }

    @Override
    public int getOrder() {
        return ConfigFileApplicationListener.DEFAULT_ORDER + 2;
    }

    private void init(String groupId, String dataId) {
        //初始化进行一次配置获取
        try {
            SysConfig config = DBUtils.getObject(groupId, dataId);
            this.properties.load(new StringReader(config.getDescription()));
            if (null != environment && environment instanceof AbstractEnvironment) {
                MutablePropertySources sources = ((AbstractEnvironment) this.environment).getPropertySources();
                sources.addLast(new PropertiesPropertySource(DIAMOND_PROPERTIES, this.properties));
            }
        } catch (IOException ioe) {
            throw new RuntimeException("获取diamond配置异常", ioe);
        }
    }

    /**
     * 刷新配置
     *
     * @param configInfo
     */
    private synchronized void refreshConfig(String configInfo) {
        try {
            properties.load(new StringReader(configInfo));
        } catch (IOException ioe) {
            throw new RuntimeException("刷新配置错误：" + configInfo, ioe);
        }

        if (null != environment && environment instanceof AbstractEnvironment) {
            MutablePropertySources sources = ((AbstractEnvironment) this.environment).getPropertySources();
            sources.remove(DIAMOND_PROPERTIES);
            sources.addLast(new PropertiesPropertySource(DIAMOND_PROPERTIES, this.properties));
        }
    }
}