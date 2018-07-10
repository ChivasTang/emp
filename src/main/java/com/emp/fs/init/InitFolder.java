package com.emp.fs.init;

import com.emp.fs.service.ICSVService;
import com.emp.fs.util.ClassPathUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

@Component
@Order(1)
public class InitFolder implements ApplicationRunner {
    @Resource
    private ICSVService csvService;

    private File FOLDER = new File(ClassPathUtils.getClassPath() + "/csv/output");

    @Override
    public void run(ApplicationArguments args) {
        csvService.initializeFolder(FOLDER);
    }
}
