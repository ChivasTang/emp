DROP DATABASE IF EXISTS emp;
CREATE DATABASE IF NOT EXISTS emp
  DEFAULT CHARSET utf8
  COLLATE utf8_general_ci;

USE emp;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id`            INT(11)      NOT NULL AUTO_INCREMENT
  COMMENT '社員番号',
  `name`          VARCHAR(50)  NOT NULL DEFAULT 'name'
  COMMENT '氏名',
  `kana`          VARCHAR(50)  NOT NULL DEFAULT 'kana'
  COMMENT '氏名かな',
  `sex`           CHAR(1)      NOT NULL DEFAULT '男'
  COMMENT '性別：１．男；２．女',
  `birthday`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '生年月日',
  `address`       VARCHAR(100) NOT NULL DEFAULT 'address'
  COMMENT '住所',
  `telephone`     VARCHAR(20)           DEFAULT NULL
  COMMENT '固定電話',
  `mobile`        VARCHAR(20)  NOT NULL DEFAULT 'mobile'
  COMMENT '携帯電話',
  `email`         VARCHAR(255) NOT NULL DEFAULT 'email'
  COMMENT 'EMAIL',
  `hireday`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '入社年月日',
  `department_id` INT(11)      NOT NULL DEFAULT '1'
  COMMENT '所属部門番号',
  `position_id`   INT(11)      NOT NULL DEFAULT '9'
  COMMENT '職位番号',
  `created`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '作成タイムスタンプ',
  `updated`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新タイムスタンプ',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT ='社員TBL';

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id`      INT(11)     NOT NULL AUTO_INCREMENT
  COMMENT '部門番号',
  `name`    VARCHAR(50) NOT NULL DEFAULT 'name'
  COMMENT '部門名称',
  `created` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '作成タイムスタンプ',
  `updated` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新タイムスタンプ',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT ='部門TBL';

INSERT INTO `department` (id, name) VALUES ('1', '人事部');
INSERT INTO `department` (id, name) VALUES ('2', '営業部');
INSERT INTO `department` (id, name) VALUES ('3', '財務部');
INSERT INTO `department` (id, name) VALUES ('4', '開発1部');
INSERT INTO `department` (id, name) VALUES ('5', '開発2部');

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position` (
  `id`      INT(11)     NOT NULL AUTO_INCREMENT
  COMMENT '職位番号',
  `name`    VARCHAR(50) NOT NULL DEFAULT 'name'
  COMMENT '職位名称',
  `created` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '作成タイムスタンプ',
  `updated` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新タイムスタンプ',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT ='職位TBL';

INSERT INTO `position` (id, name) VALUES ('1', '社長');
INSERT INTO `position` (id, name) VALUES ('2', '副社長');
INSERT INTO `position` (id, name) VALUES ('3', '本部長');
INSERT INTO `position` (id, name) VALUES ('4', '部長');
INSERT INTO `position` (id, name) VALUES ('5', '次長');
INSERT INTO `position` (id, name) VALUES ('6', '課長');
INSERT INTO `position` (id, name) VALUES ('7', '係長');
INSERT INTO `position` (id, name) VALUES ('8', '主任');
INSERT INTO `position` (id, name) VALUES ('9', '平社員');