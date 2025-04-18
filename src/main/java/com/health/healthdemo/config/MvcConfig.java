package com.health.healthdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/studentform").setViewName("studentform");
        registry.addViewController("/qualification").setViewName("qualification");
        registry.addViewController("/parentsinfo").setViewName("parentsinfo");
        registry.addViewController("/quota").setViewName("quota");
        registry.addViewController("/fileupload").setViewName("fileupload");
        registry.addViewController("/preview").setViewName("preview");

//        registry.addViewController("/Menus").setViewName("Menus");

    }

}