package com.emp.fs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class ClassPathUtils {
    private static final Logger logger = LoggerFactory.getLogger(ClassPathUtils.class);

    public static String getClassPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
            logger.info("CLASSPATH:" + path.getAbsolutePath());
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
        return path != null ? path.getAbsolutePath() : null;
    }
}
