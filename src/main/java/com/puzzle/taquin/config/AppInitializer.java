package com.puzzle.taquin.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Filtre d'encodage UTF-8 : force les requêtes et les réponses
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encodingFilter", filter);
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");

        // Contexte racine (Spring Data, Hibernate, ...)
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(PersistenceConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Contexte web (Spring MVC, Thymeleaf, ...)
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(AppConfig.class);

        DispatcherServlet servlet = new DispatcherServlet(webContext);
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
       
        ServletRegistration.Dynamic h2Servlet = servletContext.addServlet("h2Console", new org.h2.server.web.JakartaWebServlet());
        h2Servlet.addMapping("/h2-console/*");
        h2Servlet.setLoadOnStartup(2);
    
    }
}