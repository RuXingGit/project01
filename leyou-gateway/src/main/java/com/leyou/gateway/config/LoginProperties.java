package com.leyou.gateway.config;


import com.leyou.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PublicKey;

/**
 * @author RuXing
 * @create 2020-04-03 17:27
 */
@Component
@ConfigurationProperties(prefix = "leyou.jwt")
public class LoginProperties {

    // 公钥位置
    private String publicKeyPath;

    // cookieName
    private String cookieName;

    // 公钥
    private PublicKey publicKey;

    public static final Logger LOGGER = LoggerFactory.getLogger(LoginProperties.class);

    // 初始化公钥
    @PostConstruct
    public void init() {
        try {
            File publicKeyFile = new File(publicKeyPath);

            if (publicKeyFile.exists()) {
                publicKey = RsaUtils.getPublicKey(publicKeyPath);
            }
        } catch (Exception e) {
            LOGGER.error("公钥初始化失败", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

}
