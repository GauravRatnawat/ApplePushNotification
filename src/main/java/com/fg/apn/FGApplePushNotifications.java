package com.fg.apn;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fg.apn.utility.GeneralUtil;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableCaching
@EnableScheduling
public class FGApplePushNotifications {
    
  public FGApplePushNotifications() {
        super();
    }

public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(FGApplePushNotifications.class);
    springApplication.run(args);
    springApplication.setBannerMode(Mode.OFF);
    GeneralUtil.printMemory();
  }

}
