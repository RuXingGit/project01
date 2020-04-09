package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Thymeleaf组件
 * Context             运行上下文
 * 用来保存模型数据，作为渲染时的数据使用
 * TemplateResolver    模板解析器
 * 用来读取模板的配置（模板位置、模板名称、模板文件类型...）
 * TemplateEngine      模板引擎
 * 通过运行上下文模板解析器解析模板
 * <p>
 * templateEngine.process("模板名",context,writer);
 * <p>
 * 页面静态化
 *
 * @author RuXing
 * @create 2020-04-02 12:49
 */
@Service
public class GoodsHtmlService {
    @Autowired
    private TemplateEngine templateEngine;


    public void createHtml(Long spuId, Map<String, Object> data) {
        // 必要参数
        String templateName = "item";
        String path = "D:\\nginx-1.14.0\\html\\item\\" + spuId + ".html";

        // 初始化运行上下文
        Context context = new Context();
        // 设置数据模型
        context.setVariables(data);

        PrintWriter writer = null;
        try {
            File file = new File(path);
            writer = new PrintWriter(file);

            templateEngine.process(templateName, context, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    // 删除无效的页面
    public void deleteHtml(Long id) {
        String path = "D:\\nginx-1.14.0\\html\\item\\" + id + ".html";
        File file = new File(path);
        file.deleteOnExit();
    }
}
