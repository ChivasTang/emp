package com.emp.fs.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class InitIndex implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(InitIndex.class);

    @Value("${loginurl}")
    private String loginUrl;
    @Value("${googleexcute}")
    private String googleExcutePath;
    @Value("${openurl}")
    private boolean isOpen;

    @Override
    public void run(ApplicationArguments args) {
        if (isOpen) {
            String cmd = googleExcutePath + " " + loginUrl;
            Runtime run = Runtime.getRuntime();
            try {
                run.exec(cmd);
                logger.info("ブラウザを開き、サービスの起動に成功しました。。。");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }
}
