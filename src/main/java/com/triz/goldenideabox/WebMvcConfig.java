package com.triz.goldenideabox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${goldenideabox.path.avatar}")
    private String avatar;

    @Value("${goldenideabox.path.file}")
    private String file;

    @Value("${goldenideabox.path.document}")
    private String document;

    @Value("${goldenideabox.path.temp}")
    private String temp;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //文件磁盘图片url 映射
        //配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        registry.addResourceHandler("/avatar/**").addResourceLocations("file:" + avatar);
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + file);
        registry.addResourceHandler("/document/**").addResourceLocations("file:" + document);
        registry.addResourceHandler("/temp/**").addResourceLocations("file:" + temp);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }


}
