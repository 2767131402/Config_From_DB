package com.zhenglei;

import com.zhenglei.config.DiamondConfListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(new DiamondConfListener());
        application.run(args);
    }
}
