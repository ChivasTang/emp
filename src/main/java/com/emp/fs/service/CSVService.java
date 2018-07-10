package com.emp.fs.service;

import com.emp.fs.dao.DepartmentMapper;
import com.emp.fs.dao.EmployeeMapper;
import com.emp.fs.dao.PositionMapper;
import com.emp.fs.dto.Basic;
import com.emp.fs.dto.Contact;
import com.emp.fs.dto.Dep;
import com.emp.fs.entity.Department;
import com.emp.fs.entity.Employee;
import com.emp.fs.entity.Position;
import com.emp.fs.util.ClassPathUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "csvService")
public class CSVService implements ICSVService {
    private static final Logger logger = LoggerFactory.getLogger(ICSVService.class);
    private static final String FILEPATH_PREFIX = ClassPathUtils.getClassPath() + "/csv";
    private static final String FILEPATH_PREFIX_IN = "/input/employee-";
    private static final String FILEPATH_SUFFIX_IN = "-info.csv";
    private static final String FILEPATH_PREFIX_OUT = "/output/";
    private static final String FILEPATH_SUFFIX_OUT = "-info-converted.csv";
    private static final String ZIP_SUFFIX = ".zip";

    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private PositionMapper positionMapper;

    @Override
    public void initializeDB(String filePath) {
        logger.info("データベースを初期化開始。。。");
        Connection connection = null;
        File file = new File(filePath);
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            Reader reader = new BufferedReader(inputStreamReader);
            ScriptRunner runner = new ScriptRunner(connection);
            runner.runScript(reader);
        } catch (ClassNotFoundException | SQLException | UnsupportedEncodingException | FileNotFoundException e) {
            logger.error(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        logger.info("データベースを初期化完了。。。");
    }

    @Override
    public void initializeFolder(File file) {
        logger.info("フォルダーの初期化開始。。。");
        if (file == null) {
            logger.error("ご指定されたフォルダーは存在しません。。。");
        } else {
            //是文件？
            if (file.isFile()) {
                logger.info("下記ファイルを削除している：" + file.getAbsolutePath());
                file.delete();
            } else if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    logger.info(fileList.length + "個のファイルが存在している。");
                    for (File file2 : fileList) {
                        initializeFolder(file2);
                    }
                }
            }
        }
        logger.info("フォルダーの初期化完了しました。");
    }

    @Override
    public List<Basic> readCSVBasic(String fileName) {
        BufferedReader br = getBr(fileName);
        String line;
        String everyLine;
        List<Basic> basicList = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                everyLine = line;
                String[] str = everyLine.split(",");
                Basic basic = new Basic();
                basic.setName(str[0]);
                basic.setKana(str[1]);
                basic.setSex(str[2]);
                String[] str_date = str[3].split("/");

                Date birthday = new Date(Integer.parseInt(str_date[0]) - 1900, Integer.parseInt(str_date[1]) - 1, Integer.parseInt(str_date[2]), 00, 00, 00);
                basic.setBirthday(birthday);
                basicList.add(basic);
                logger.info(basic.toString());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return basicList;
    }

    @Override
    public List<Contact> readCSVContact(String fileName) {
        BufferedReader br = getBr(fileName);
        String line;
        String everyLine;
        List<Contact> contactList = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                everyLine = line;
                String[] str = everyLine.split(",");
                Contact contact = new Contact();
                contact.setName(str[0]);
                contact.setAddress1(str[1]);
                contact.setAddress2(str[2]);
                contact.setTelephone(str[3]);
                contact.setMobile(str[4]);
                contact.setEmail(str[5]);

                contactList.add(contact);
                logger.info(contact.toString());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return contactList;
    }

    @Override
    public List<Dep> readCSVDep(String fileName) {
        BufferedReader br = getBr(fileName);
        String line;
        String everyLine;
        List<Dep> depList = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                everyLine = line;
                String[] str = everyLine.split(",");
                Dep dep = new Dep();
                dep.setName(str[0]);
                dep.setDepartmentName(str[1]);
                dep.setPositionName(str[2]);
                String[] str_date = str[3].split("/");
                Date hireday = new Date(Integer.parseInt(str_date[0]) - 1900, Integer.parseInt(str_date[1]) - 1, Integer.parseInt(str_date[2]), 00, 00, 00);
                dep.setHiredate(hireday);
                depList.add(dep);
                logger.info(dep.toString());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return depList;
    }

    @Override
    public void insertAll(String filePathBasicName, String filePathContactName, String filePathDepName) {
        List<Basic> basicList = readCSVBasic(filePathBasicName);
        List<Contact> contactList = readCSVContact(filePathContactName);
        List<Dep> depList = readCSVDep(filePathDepName);
        for (Basic basic : basicList) {
            Employee employee = new Employee();
            employee.setName(basic.getName());
            employee.setKana(basic.getKana());
            employee.setSex(basic.getSex());
            employee.setBirthday(basic.getBirthday());

            for (Contact contact : contactList) {
                if (employee.getName().equals(contact.getName())) {
                    employee.setAddress(contact.getAddress1() + " " + contact.getAddress2());
                    employee.setTelephone(contact.getTelephone());
                    employee.setMobile(contact.getMobile());
                    employee.setEmail(contact.getEmail());
                }
            }

            for (Dep dep : depList) {
                if (employee.getName().equals(dep.getName())) {
                    employee.setHireday(dep.getHiredate());
                    employee.setDepartmentId(departmentMapper.selectByName(dep.getDepartmentName()).getId());
                    employee.setPositionId(positionMapper.selectByName(dep.getPositionName()).getId());
                }
            }

            employeeMapper.insert(employee);
            logger.info(employee.toString() + "は、データベースに保存されました。");
        }
    }

    @Override
    public List<Employee> findAllEmployee() {
        return employeeMapper.selectAll();
    }

    @Override
    public void writeCSVEmployee(String fileName) {
        List<Employee> employeeList = employeeMapper.selectAll();
        BufferedWriter bw = null;
        try {
            if (fileName.isEmpty()) {
                fileName = "employee";
            }
            File csv = new File(FILEPATH_PREFIX + FILEPATH_PREFIX_OUT + fileName + FILEPATH_SUFFIX_OUT);
            bw = new BufferedWriter(new FileWriter(csv, true));
            bw.newLine();
            for (Employee employee : employeeList) {
                String str = "";
                str += employee.getId() + ",";
                str += employee.getDepartmentId() + ",";
                str += employee.getName() + ",";
                str += employee.getKana() + ",";
                str += employee.getSex() + ",";
                str += employee.getBirthday() + ",";
                str += employee.getAddress() + ",";
                str += employee.getTelephone() + ",";
                str += employee.getMobile() + ",";
                str += employee.getEmail() + ",";
                str += employee.getPositionId() + ",";
                str += employee.getHireday() + "\r\n";
                bw.write(str);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void writeCSVDepartment(String fileName) {
        List<Department> departmentList = departmentMapper.selectAll();
        BufferedWriter bw = null;
        try {
            if (fileName.isEmpty()) {
                fileName = "department";
            }
            File csv = new File(FILEPATH_PREFIX + FILEPATH_PREFIX_OUT + fileName + FILEPATH_SUFFIX_OUT);
            bw = new BufferedWriter(new FileWriter(csv, true));
            bw.newLine();
            for (Department department : departmentList) {
                String str = "";
                str += department.getId() + ",";
                str += department.getName() + "\r\n";
                bw.write(str);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void writeCSVPosition(String fileName) {
        List<Position> positionList = positionMapper.selectAll();
        BufferedWriter bw = null;
        try {
            if (fileName.isEmpty()) {
                fileName = "position-info-converted";
            }
            File csv = new File(FILEPATH_PREFIX + FILEPATH_PREFIX_OUT + fileName + FILEPATH_SUFFIX_OUT);
            bw = new BufferedWriter(new FileWriter(csv, true));
            bw.newLine();
            for (Position position : positionList) {
                String str = "";
                str += position.getId() + ",";
                str += position.getName() + "\r\n";
                bw.write(str);
            }
            bw.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void prepareZipWithPwd(String[] fileNames, String zipName, String password) {
        try {
            ZipFile zipFile = new ZipFile(FILEPATH_PREFIX + FILEPATH_PREFIX_OUT + zipName + ZIP_SUFFIX);
            ArrayList<File> filesToAdd = new ArrayList<>();
            for (String fileName : fileNames) {
                filesToAdd.add(new File(FILEPATH_PREFIX + FILEPATH_PREFIX_OUT + fileName + FILEPATH_SUFFIX_OUT));
            }
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(password);
            zipFile.addFiles(filesToAdd, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private static BufferedReader getBr(String fileName) {
        String filePath = FILEPATH_PREFIX + FILEPATH_PREFIX_IN + fileName + FILEPATH_SUFFIX_IN;
        FileInputStream fileInputStream;
        InputStreamReader streamReader;
        BufferedReader br = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            streamReader = new InputStreamReader(fileInputStream, "UTF-8");
            br = new BufferedReader(streamReader);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return br;
    }
}
