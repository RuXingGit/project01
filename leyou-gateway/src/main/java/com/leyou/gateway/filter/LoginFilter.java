package com.leyou.gateway.filter;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.LoginProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author RuXing
 * @create 2020-04-04 11:14
 */
@Component
public class LoginFilter extends ZuulFilter {
    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        // 可访问的白名单
        List<String> allowPath = filterProperties.getAllowPath();
        // 获取运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        // 获取请求地址   （包含域名的）
        String url = request.getRequestURL().toString();


        // 检查
        for (String path:allowPath){
            // 如果包含了可访问路径就不进行过滤
            if(StringUtils.contains(url,path)){
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 初始化zuul运行长下文
        RequestContext context = RequestContext.getCurrentContext();
        // 获取request对象
        HttpServletRequest request = context.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, loginProperties.getCookieName());


        if(StringUtils.isBlank(token)){

        }

        // 解析token
        try {
            UserInfo user = JwtUtils.getInfoFromToken(token, loginProperties.getPublicKey());
        } catch (Exception e) {
            // 没有成功获取token,解析就会失败
            context.setSendZuulResponse(false);     // 不进行转发
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value()); // 响应状态码
        }


        return null;
    }
}
