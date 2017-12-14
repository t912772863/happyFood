/*
Navicat MySQL Data Transfer

Source Server         : localmysql
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : happyfood

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2017-12-14 19:13:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for button
-- ----------------------------
DROP TABLE IF EXISTS `button`;
CREATE TABLE `button` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `NAME` varchar(50) NOT NULL COMMENT '按钮名称',
  `TYPE` varchar(30) NOT NULL COMMENT '按钮类型',
  `LEVEL` int(1) NOT NULL COMMENT '按钮等级',
  `ORDERS` int(2) DEFAULT NULL COMMENT '在本级按钮的排序',
  `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '父级按钮的ID',
  `USE_STATUS` int(2) NOT NULL COMMENT '是否在微信上使用中: 0否, 1是',
  `STATUS` int(2) NOT NULL COMMENT '数据状态: 1正常, -1删除',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of button
-- ----------------------------

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES ('1', '123');
