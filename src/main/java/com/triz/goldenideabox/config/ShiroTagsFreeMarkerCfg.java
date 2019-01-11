package com.triz.goldenideabox.config;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.TemplateModelException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Component
public class ShiroTagsFreeMarkerCfg {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @PostConstruct
    public void setSharedVariable() throws TemplateModelException {

        freeMarkerConfigurer.getConfiguration().setSharedVariable("shiro", new ShiroTags());
    }

}
