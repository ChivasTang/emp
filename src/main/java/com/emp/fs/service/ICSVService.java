package com.emp.fs.service;

import com.emp.fs.dto.Basic;
import com.emp.fs.dto.Contact;
import com.emp.fs.dto.Dep;
import com.emp.fs.entity.Employee;

import java.io.File;
import java.util.List;

public interface ICSVService {
    void initializeDB(String filePath);

    void initializeFolder(File file);

    List<Basic> readCSVBasic(String fileName);

    List<Contact> readCSVContact(String fileName);

    List<Dep> readCSVDep(String fileName);

    void insertAll(String filePathBasicName, String filePathContactName, String filePathDepName);

    List<Employee> findAllEmployee();

    void writeCSVEmployee(String fileName);

    void writeCSVDepartment(String fileName);

    void writeCSVPosition(String fileName);

    void prepareZipWithPwd(String[] fileNames, String zipName, String password);
}
