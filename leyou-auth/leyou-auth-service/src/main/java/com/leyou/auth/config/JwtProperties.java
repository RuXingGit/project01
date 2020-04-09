package com.leyou.auth.config;

import com.leyou.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import sun.security.provider.PolicySpiFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author RuXing
 * @create 2020-04-03 17:27
 */
@Component
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {
    // 密钥
    private String secret;
    // 公钥位置
    private String publicKeyPath;
    // 私钥位置
    private String privateKeyPath;
    // token过期时间
    private Integer expire;
    // cookieName
    private String cookieName;
    // 公钥
    private PublicKey publicKey;
    // 私钥
    private PrivateKey privateKey;

    public static final Logger LOGGER = LoggerFactory.getLogger(JwtProperties.class);

    /**
     * @PostConstruct:  在构造方法执行后这个方法会执行
     */
    @PostConstruct
    public void init(){
        try {
            File privateKeyFile = new File(privateKeyPath);
            File publicKeyFile = new File(publicKeyPath);
            boolean bool = !privateKeyFile.exists()||!publicKeyFile.exists();
            if(bool){
                RsaUtils.generateKey(publicKeyPath,privateKeyPath,secret);
            }

            publicKey = RsaUtils.getPublicKey(publicKeyPath);
            privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        } catch (Exception e) {
            LOGGER.error("公钥和私钥初始化失败",e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
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

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
