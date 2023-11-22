package com.example.coderlab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path certifyUploadDir = Paths.get("./certify-images");
        String certifyUploadPath1 = certifyUploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/certify-images/**").addResourceLocations("file:/"+certifyUploadPath1+"/");

        exposeDirectory("avt-images", registry);
        exposeDirectory("company-images", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
    }

}
