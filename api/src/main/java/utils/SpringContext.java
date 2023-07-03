package utils;

import org.springframework.beans.BeansException;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"amq"})
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext context;

    public static ApplicationContext initializeContext(final String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringContext.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        return springApplication.run(args);
    }
     
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }     
 
    public static ApplicationContext getContext() {
        return context;
    }

    public static void closeContext(ApplicationContext context) {
        ((ConfigurableApplicationContext) context).close();
    }
}