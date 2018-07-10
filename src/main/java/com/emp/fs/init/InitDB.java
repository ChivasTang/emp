package com.emp.fs.init;

import com.emp.fs.service.ICSVService;
import com.emp.fs.util.ClassPathUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(2)
public class InitDB implements ApplicationRunner {
    @Resource
    private ICSVService csvService;

    private static final String SQL_PATH = ClassPathUtils.getClassPath() + "/emp.sql";

    @Override
    public void run(ApplicationArguments args) {
        csvService.initializeDB(SQL_PATH);
    }
}
