/*
Navicat MySQL Data Transfer

Source Server         : my_ten_cloud
Source Server Version : 50638
Source Host           : 118.126.115.206:3306
Source Database       : happyfood

Target Server Type    : MYSQL
Target Server Version : 50638
File Encoding         : 65001

Date: 2019-06-05 17:30:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for button
-- ----------------------------
DROP TABLE IF EXISTS `button`;
CREATE TABLE `button` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `level` tinyint(3) DEFAULT NULL,
  `orders` tinyint(4) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `useStatus` tinyint(2) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `class_id` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prepare_time` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `id` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tag` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `people_num` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` text COLLATE utf8_unicode_ci,
  `cooking_time` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `detail_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for dish_material
-- ----------------------------
DROP TABLE IF EXISTS `dish_material`;
CREATE TABLE `dish_material` (
  `dish_id` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `amount` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for dish_procedure
-- ----------------------------
DROP TABLE IF EXISTS `dish_procedure`;
CREATE TABLE `dish_procedure` (
  `dish_id` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pic` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `orders` tinyint(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for dish_variety
-- ----------------------------
DROP TABLE IF EXISTS `dish_variety`;
CREATE TABLE `dish_variety` (
  `class_id` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_id` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for event
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `to_user_name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `from_user_name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `msg_create_time` datetime DEFAULT NULL,
  `msg_type` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `event` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `event_key` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ticket` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `latitude` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `longitude` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `precision` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for fans
-- ----------------------------
DROP TABLE IF EXISTS `fans`;
CREATE TABLE `fans` (
  `open_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nickname` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `city` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `country` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `province` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `language` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `head_img_url` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL,
  `subscribe_time` datetime DEFAULT NULL,
  `union_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `remark` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `group_id` tinyint(3) DEFAULT NULL,
  `tag_id_list` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `subscribe_status` tinyint(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `to_user_name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `from_user_name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `msg_create_time` datetime DEFAULT NULL,
  `msg_type` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pic_url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `media_id` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `format` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `recognition` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `thumb_media_id` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location_x` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location_y` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `scale` tinyint(3) DEFAULT NULL,
  `label` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `msg_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `test` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pass_word` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mail` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `mobile` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
