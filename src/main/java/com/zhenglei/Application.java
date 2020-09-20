package com.zhenglei;

import com.zhenglei.config.DiamondConfListener;
import com.zhenglei.config.DiamondConfListener_1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
//        application.addListeners(new DiamondConfListener());
        application.addListeners(new DiamondConfListener_1());
        application.run(args);
    }
}
