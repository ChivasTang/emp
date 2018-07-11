package com.emp.fs.web;

import com.emp.fs.entity.Employee;
import com.emp.fs.service.ICSVService;
import com.emp.fs.util.ClassPathUtils;
import com.emp.fs.util.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/")
public class CSVController {
    private static Logger logger = LoggerFactory.getLogger(CSVController.class);
    private static final String BASIC_IN = "basic";
    private static final String CONTACT_IN = "contact";
    private static final String DEP_IN = "department";
    private static final String EMPLOYEE_OUT = "employee";
    private static final String DEPARTMENT_OUT = "department";
    private static final String POSITION_OUT = "position";

    private static final String ZIP_PREFIX = ClassPathUtils.getClassPath() + "/csv/output/";
    private static final String ZIP_SUFFIX = ".zip";
    private static final String ZIP_NAME = "EmployeeInfoConverted";

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    @Resource
    private ICSVService csvService;

    @Resource
    private JavaMailSender mailSender;

    @GetMapping("/csv")
    public String csv(Model model) {
        csvService.insertAll(BASIC_IN, CONTACT_IN, DEP_IN);
        csvService.writeCSVEmployee(EMPLOYEE_OUT);
        logger.info("社員情報CSVファイルを作成しました。。。");
        csvService.writeCSVDepartment(DEPARTMENT_OUT);
        logger.info("部門情報CSVファイルを作成しました。。。");
        csvService.writeCSVPosition(POSITION_OUT);
        logger.info("職位情報CSVファイルを作成しました。。。");
        model.addAttribute("employeeList", csvService.findAllEmployee());
        return "csv";
    }


    @ResponseBody
    @PostMapping("/mail")
    public Model mail(Model model) throws MailException {
        String[] fileNames = {POSITION_OUT, DEPARTMENT_OUT, EMPLOYEE_OUT};
        String zipPwd = RandomStringUtils.getPassword();
        model.addAttribute("zipPwd", "解凍用パスワードは：" + zipPwd);
        csvService.prepareZipWithPwd(fileNames, ZIP_NAME, zipPwd);
        List<Employee> employeeList = csvService.findAllEmployee();

        for (Employee employee : employeeList) {
            MimeMessage message = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(username);
                helper.setTo(employee.getEmail());
                helper.setSubject("社員情報処理結果の通知");
                helper.setText("ZIPファイルの解凍用パスワードは、<---" + zipPwd + "--->", true);

                FileSystemResource file = new FileSystemResource(new File(ZIP_PREFIX + ZIP_NAME + ZIP_SUFFIX));
                helper.addAttachment("EmployeeInfoConverted.zip", file);
                mailSender.send(message);
                model.addAttribute("msg", "送信を行いました。メールボクスをチェックしてください。");
            } catch (MessagingException e) {
                logger.error(e.getMessage());
            }
        }
        model.addAttribute("employeeList", employeeList);
        return model;
    }
}
