/*
Navicat MySQL Data Transfer

Source Server         : 60.205.188.180_3306
Source Server Version : 50717
Source Host           : 60.205.188.180:3306
Source Database       : Warrants

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-01-17 17:06:01
*/

SET FOREIGN_KEY_CHECKS=0;



alter table News add `lang` varchar(16) not null default '_cn' COMMENT '语言'; #添加语言字段

alter table News modify body mediumtext COMMENT '新闻内容';

alter table resource_auth add `write` int not null default 0 COMMENT '是否具有写权限 0-否 1-是'; #添资源权限写字段

UPDATE resource_auth SET `write` = 0;

DELETE FROM resource_auth WHERE resource_id = 3 AND auth_id = 1;

INSERT INTO  resource_auth (resource_id,auth_id,`write`,create_time) VALUES (3,1,1,NOW());
 
INSERT INTO LogsType (Id,`Name`) VALUES (21,'子账户管理');




SET FOREIGN_KEY_CHECKS=1;
