package com.loko.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafForEmailTemplateConfig {
    @Value("${application.mail.email-template-path}")
    private String emailTemplatePath;

    @Bean
    ITemplateResolver fileTemplateResolver() {
        System.out.println("EMAIL TEMPLATE PATH -> " + emailTemplatePath);
        FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();

        // 1. ตั้งค่า Path ที่จะไปหาไฟล์
        fileTemplateResolver.setPrefix(emailTemplatePath);

        // 2. ตั้งค่า Suffix ของไฟล์
        fileTemplateResolver.setSuffix(".html");

         // 3. ตั้งค่าโหมดของ Template
         fileTemplateResolver.setTemplateMode(TemplateMode.HTML);

         // 4. ตั้งค่า Character Encoding
         fileTemplateResolver.setCharacterEncoding("UTF-8");

         // 5. ตั้งค่าให้ Resolver นี้มีความสำคัญเป็นอันดับแรก
         fileTemplateResolver.setOrder(2);
         
         // 6. บอกให้ Resolver นี้ทำงานได้ แม้ว่าจะไม่มีไฟล์อยู่จริง (ป้องกัน Error ตอนเริ่มโปรแกรม)
         fileTemplateResolver.setCheckExistence(true);
        
         return fileTemplateResolver;
    }
}
