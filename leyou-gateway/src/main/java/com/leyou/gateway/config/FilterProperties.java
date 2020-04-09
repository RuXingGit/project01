package com.leyou.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-04-04 11:36
 */
@Component
@ConfigurationProperties(prefix = "leyou.filter")
public class FilterProperties {
    private List<String> allowPath;

    public List<String> getAllowPath() {
        return allowPath;
    }

    public void setAllowPath(List<String> allowPath) {
        this.allowPath = allowPath;
    }
}
