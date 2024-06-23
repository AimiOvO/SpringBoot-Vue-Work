/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80200 (8.2.0)
 Source Host           : localhost:3306
 Source Schema         : db_gyhr

 Target Server Type    : MySQL
 Target Server Version : 80200 (8.2.0)
 File Encoding         : 65001

 Date: 28/05/2024 23:59:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for facility_park
-- ----------------------------
DROP TABLE IF EXISTS `facility_park`;
CREATE TABLE `facility_park`  (
  `park_id` int NOT NULL AUTO_INCREMENT COMMENT '车位Id',
  `park_type` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '车位类型 0地上 1地下',
  `park_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '车位名称',
  `park_status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '车位使用状态 0未使用 1已使用',
  `park_num` int NULL DEFAULT NULL COMMENT '车位序号',
  PRIMARY KEY (`park_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '车位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of facility_park
-- ----------------------------
INSERT INTO `facility_park` VALUES (3, '0', 'A001', '1', 1);
INSERT INTO `facility_park` VALUES (4, '1', 'B001', '0', 2);
INSERT INTO `facility_park` VALUES (5, '0', 'C001', '1', 3);

-- ----------------------------
-- Table structure for fee_parkfee
-- ----------------------------
DROP TABLE IF EXISTS `fee_parkfee`;
CREATE TABLE `fee_parkfee`  (
  `parkfee_id` int NOT NULL AUTO_INCREMENT COMMENT '停车费Id',
  `fee_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '费用订单编号',
  `park_id` int NULL DEFAULT NULL COMMENT '停车位Id',
  `customer_id` int NULL DEFAULT NULL COMMENT '用户Id',
  `pay_month` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缴费年月',
  `pay_money` decimal(18, 2) NULL DEFAULT NULL COMMENT '缴费金额',
  `pay_status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缴费状态 0未缴费 1已缴费',
  `pay_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '支付单编号',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`parkfee_id`) USING BTREE,
  INDEX `PFPfk1`(`park_id` ASC) USING BTREE,
  INDEX `PFCfk1`(`customer_id` ASC) USING BTREE,
  CONSTRAINT `PFCfk1` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `PFPfk1` FOREIGN KEY (`park_id`) REFERENCES `facility_park` (`park_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '停车费表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fee_parkfee
-- ----------------------------
INSERT INTO `fee_parkfee` VALUES (3, 'PKFNo3', 3, 2, '2024-02', 12345.00, '1', '2024022822001468340503047647', '2024-02-28 22:27:42');
INSERT INTO `fee_parkfee` VALUES (4, 'PKFNo202402286232', 3, 2, '2024-03', 123.00, '0', NULL, NULL);

-- ----------------------------
-- Table structure for fee_power
-- ----------------------------
DROP TABLE IF EXISTS `fee_power`;
CREATE TABLE `fee_power`  (
  `powerfee_id` int NOT NULL AUTO_INCREMENT COMMENT '电费Id',
  `fee_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '费用订单编号',
  `home_id` int NULL DEFAULT NULL COMMENT '房屋Id',
  `customer_id` int NULL DEFAULT NULL COMMENT '用户Id',
  `pay_month` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缴费年月',
  `pay_money` decimal(18, 2) NULL DEFAULT NULL COMMENT '缴费金额',
  `power_num` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表显',
  `pay_status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缴费状态 0未缴费 1已缴费',
  `pay_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '支付单编号',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`powerfee_id`) USING BTREE,
  INDEX `PHfk1`(`home_id` ASC) USING BTREE,
  INDEX `PCfk2`(`customer_id` ASC) USING BTREE,
  CONSTRAINT `PCfk2` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `PHfk1` FOREIGN KEY (`home_id`) REFERENCES `house_home` (`home_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '电费表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fee_power
-- ----------------------------
INSERT INTO `fee_power` VALUES (2, 'PWFNo111', 1, 2, '2024-02', 1000.00, '100', '1', '2024022922001468340503048911', '2024-02-29 12:02:01');
INSERT INTO `fee_power` VALUES (3, 'PWFNo3', 1, 2, '2024-03', 1200.00, '12', '1', '2024022822001468340503045453', '2024-02-28 22:39:08');
INSERT INTO `fee_power` VALUES (4, 'PWFNo4', 1, 2, '2024-02', 12.12, '123', '1', '2024022822001468340503048907', '2024-02-28 22:36:45');
INSERT INTO `fee_power` VALUES (6, 'PWFNo20240228152', 1, 2, '2024-03', 123.00, '123', '0', NULL, NULL);
INSERT INTO `fee_power` VALUES (7, 'PWFNo20240228312', 1, 2, '2024-07', 123.00, '123', '0', NULL, NULL);
INSERT INTO `fee_power` VALUES (8, 'PWFNo202403014222', 4, 2, '2024-06', 12.00, '6', '0', NULL, NULL);
INSERT INTO `fee_power` VALUES (9, 'PWFNo202405186032', 1, 2, '2024-12', 2.00, '1', '0', NULL, NULL);

-- ----------------------------
-- Table structure for fee_rental
-- ----------------------------
DROP TABLE IF EXISTS `fee_rental`;
CREATE TABLE `fee_rental`  (
  `rentalfee_id` int NOT NULL AUTO_INCREMENT COMMENT '租费Id',
  `fee_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '费用订单编号',
  `home_id` int NULL DEFAULT NULL COMMENT '房屋Id',
  `customer_id` int NULL DEFAULT NULL COMMENT '用户Id',
  `pay_month` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缴费年月',
  `pay_money` decimal(18, 2) NULL DEFAULT NULL COMMENT '缴费金额',
  `pay_status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缴费状态 0未缴费 1已缴费',
  `pay_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '支付单编号',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`rentalfee_id`) USING BTREE,
  INDEX `RFHfk1`(`home_id` ASC) USING BTREE,
  INDEX `RFCfk2`(`customer_id` ASC) USING BTREE,
  CONSTRAINT `RFCfk2` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `RFHfk1` FOREIGN KEY (`home_id`) REFERENCES `house_home` (`home_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '租费表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fee_rental
-- ----------------------------
INSERT INTO `fee_rental` VALUES (1, 'RTFNo1', 1, 2, '2024-03', 114514.00, '0', NULL, NULL);
INSERT INTO `fee_rental` VALUES (2, 'RTFNo2', 2, 6, '2024-02', 123.00, '0', NULL, NULL);

-- ----------------------------
-- Table structure for fee_water
-- ----------------------------
DROP TABLE IF EXISTS `fee_water`;
CREATE TABLE `fee_water`  (
  `waterfee_id` int NOT NULL AUTO_INCREMENT COMMENT '水费Id',
  `fee_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '费用订单编号',
  `home_id` int NULL DEFAULT NULL COMMENT '房屋Id',
  `customer_id` int NULL DEFAULT NULL COMMENT '用户Id',
  `pay_month` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缴费年月',
  `pay_money` decimal(18, 2) NULL DEFAULT NULL COMMENT '缴费金额',
  `water_num` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表显',
  `pay_status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '缴费状态 0未缴费 1已缴费',
  `pay_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '支付单编号',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`waterfee_id`) USING BTREE,
  INDEX `WHfk1`(`home_id` ASC) USING BTREE,
  INDEX `WCfk1`(`customer_id` ASC) USING BTREE,
  CONSTRAINT `WCfk1` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `WHfk1` FOREIGN KEY (`home_id`) REFERENCES `house_home` (`home_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '水费表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fee_water
-- ----------------------------
INSERT INTO `fee_water` VALUES (1, 'WTFNo1', 1, 2, '2024-02', 123.00, '1231', '0', NULL, NULL);

-- ----------------------------
-- Table structure for house_building
-- ----------------------------
DROP TABLE IF EXISTS `house_building`;
CREATE TABLE `house_building`  (
  `build_id` int NOT NULL AUTO_INCREMENT COMMENT '楼栋Id',
  `type` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '楼栋类型 0普通房 1电梯房',
  `build_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '楼栋名称',
  `order_num` int NULL DEFAULT NULL COMMENT '楼栋序号',
  PRIMARY KEY (`build_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '楼栋表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of house_building
-- ----------------------------
INSERT INTO `house_building` VALUES (1, '0', 'A栋', 1);
INSERT INTO `house_building` VALUES (2, '1', 'B栋', 2);
INSERT INTO `house_building` VALUES (4, '0', 'C栋', NULL);

-- ----------------------------
-- Table structure for house_home
-- ----------------------------
DROP TABLE IF EXISTS `house_home`;
CREATE TABLE `house_home`  (
  `home_id` int NOT NULL AUTO_INCREMENT COMMENT '房屋Id',
  `unit_id` int NULL DEFAULT NULL COMMENT '单元Id',
  `home_num` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '房屋编号',
  `home_area` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '房屋面积',
  `status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '使用状态 0未使用 1已使用',
  PRIMARY KEY (`home_id`) USING BTREE,
  INDEX `HUfk1`(`unit_id` ASC) USING BTREE,
  CONSTRAINT `HUfk1` FOREIGN KEY (`unit_id`) REFERENCES `house_unit` (`unit_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '房屋表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of house_home
-- ----------------------------
INSERT INTO `house_home` VALUES (1, 1, 'A1-101', '75', '1');
INSERT INTO `house_home` VALUES (2, 2, 'A1-102', '75', '1');
INSERT INTO `house_home` VALUES (3, 4, 'B2-101', '100', '0');
INSERT INTO `house_home` VALUES (4, 3, 'B1-101', '100', '1');

-- ----------------------------
-- Table structure for house_unit
-- ----------------------------
DROP TABLE IF EXISTS `house_unit`;
CREATE TABLE `house_unit`  (
  `unit_id` int NOT NULL AUTO_INCREMENT COMMENT '单元Id',
  `build_id` int NULL DEFAULT NULL COMMENT '楼栋Id',
  `unit_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '单元名称',
  PRIMARY KEY (`unit_id`) USING BTREE,
  INDEX `UBfk1`(`build_id` ASC) USING BTREE,
  CONSTRAINT `UBfk1` FOREIGN KEY (`build_id`) REFERENCES `house_building` (`build_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '单元表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of house_unit
-- ----------------------------
INSERT INTO `house_unit` VALUES (1, 1, 'A-1');
INSERT INTO `house_unit` VALUES (2, 1, 'A-2');
INSERT INTO `house_unit` VALUES (3, 2, 'B-1');
INSERT INTO `house_unit` VALUES (4, 2, 'B-2');

-- ----------------------------
-- Table structure for paas_complaint
-- ----------------------------
DROP TABLE IF EXISTS `paas_complaint`;
CREATE TABLE `paas_complaint`  (
  `complaint_id` int NOT NULL AUTO_INCREMENT COMMENT '投诉id',
  `customer_id` int NULL DEFAULT NULL COMMENT '投诉人id',
  `title` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '投诉内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '投诉时间',
  `status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '处理状态 0未处理 1已处理',
  PRIMARY KEY (`complaint_id`) USING BTREE,
  INDEX `PCCfk1`(`customer_id` ASC) USING BTREE,
  CONSTRAINT `PCCfk1` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '投诉表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of paas_complaint
-- ----------------------------
INSERT INTO `paas_complaint` VALUES (8, 2, '123', '1.采用前后端分离技术。前端使用vue为整个前端系统的脚手架。后台框架使用spring boot。\r\n2.租房管理功能：租房管理功能包括对房屋租赁信息的录入、查询、修改和删除。它可以帮助房东或物业管理公司高效地管理租房业务，包括租客信息、租赁合同、租金收缴等。\r\n3.租户管理功能：租户管理功能是对租户的信息进行管理，包括租户的姓名、联系方式、身份证信息、入住时间等。这个功能可以帮助管理者更好地了解租户的情况，提供更好的服务。\r\n4.部门管理功能：部门管理功能主要是对公司的各个部门进行管理，包括部门的名称、部门职责、部门人员等信息。这个功能可以帮助管理者更好地组织和管理公司内部各个部门的工作，提高工作效率。\r\n5.维修管理功能：修管理功能主要是对物业或设施的维修进行管理，包括维修任务的分配、维修进度的跟踪、维修费用的记录等。这个功能可以帮助管理者更好地安排和监控维修工作，保证物业或设施的正常运行。\r\n', '2024-02-18 14:46:38', '1');
INSERT INTO `paas_complaint` VALUES (9, 6, '456', '1.采用前后端分离技术。前端使用vue为整个前端系统的脚手架。后台框架使用spring boot。\r\n2.租房管理功能：租房管理功能包括对房屋租赁信息的录入、查询、修改和删除。它可以帮助房东或物业管理公司高效地管理租房业务，包括租客信息、租赁合同、租金收缴等。\r\n3.租户管理功能：租户管理功能是对租户的信息进行管理，包括租户的姓名、联系方式、身份证信息、入住时间等。这个功能可以帮助管理者更好地了解租户的情况，提供更好的服务。\r\n4.部门管理功能：部门管理功能主要是对公司的各个部门进行管理，包括部门的名称、部门职责、部门人员等信息。这个功能可以帮助管理者更好地组织和管理公司内部各个部门的工作，提高工作效率。\r\n5.维修管理功能：修管理功能主要是对物业或设施的维修进行管理，包括维修任务的分配、维修进度的跟踪、维修费用的记录等。这个功能可以帮助管理者更好地安排和监控维修工作，保证物业或设施的正常运行。\r\n', '2024-02-18 17:16:44', '1');

-- ----------------------------
-- Table structure for paas_notice
-- ----------------------------
DROP TABLE IF EXISTS `paas_notice`;
CREATE TABLE `paas_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `title` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '公告内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`notice_id`) USING BTREE,
  INDEX `PNUfk1`(`user_id` ASC) USING BTREE,
  CONSTRAINT `PNUfk1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of paas_notice
-- ----------------------------
INSERT INTO `paas_notice` VALUES (4, 1, '标题5个字', '啊啊啊啊啊\n啊啊啊啊啊啊啊啊啊啊\n\n啊啊啊啊啊啊啊啊啊啊\n\n啊啊啊啊啊啊啊啊啊啊\n\n啊啊啊啊啊啊啊啊啊啊\n\n啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊\n啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', '2024-02-18 15:25:56');
INSERT INTO `paas_notice` VALUES (5, 1, '123', '123123123', '2024-02-27 11:51:51');
INSERT INTO `paas_notice` VALUES (6, 1, '123', '123123123', '2024-02-27 11:51:54');
INSERT INTO `paas_notice` VALUES (7, 1, '123', '123123', '2024-02-27 11:51:58');
INSERT INTO `paas_notice` VALUES (8, 1, '123', '123123', '2024-02-27 11:52:00');
INSERT INTO `paas_notice` VALUES (9, 1, '123123', '123123', '2024-02-27 11:52:03');
INSERT INTO `paas_notice` VALUES (10, 1, '123123', '123123123', '2024-02-27 11:52:07');
INSERT INTO `paas_notice` VALUES (11, 1, '123123', '123123123', '2024-02-27 11:52:10');
INSERT INTO `paas_notice` VALUES (12, 1, '123123', '123123123', '2024-02-27 11:52:14');
INSERT INTO `paas_notice` VALUES (13, 1, '123123', '123123123', '2024-02-27 11:52:17');

-- ----------------------------
-- Table structure for paas_repair
-- ----------------------------
DROP TABLE IF EXISTS `paas_repair`;
CREATE TABLE `paas_repair`  (
  `repair_id` int NOT NULL AUTO_INCREMENT COMMENT '维修id',
  `customer_id` int NULL DEFAULT NULL COMMENT '租户id',
  `user_id` int NULL DEFAULT NULL COMMENT '维修人员Id',
  `repair_title` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '维修标题',
  `repair_address` tinytext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '维修地址',
  `repair_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '维修内容',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `create_time` datetime NULL DEFAULT NULL COMMENT '报修时间',
  `imgurl` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '图片列表',
  `status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '维修状态 0未派修 1已派修 2已维修',
  PRIMARY KEY (`repair_id`) USING BTREE,
  INDEX `PRCfk1`(`customer_id` ASC) USING BTREE,
  INDEX `PRUfk2`(`user_id` ASC) USING BTREE,
  CONSTRAINT `PRCfk1` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `PRUfk2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '维修表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of paas_repair
-- ----------------------------
INSERT INTO `paas_repair` VALUES (1, 2, 1, '标题五个字', '啊萨达萨达撒旦1', '1.采用前后端分离技术。前端使用vue为整个前端系统的脚手架。后台框架使用spring boot。\n4.部门管理功能：部门管理功能主要是对公司的各个部门进行管理，包括部门的名称、部门职责、部门人员等信息。这个功能可以帮助管理者更好地组织和管理公司内部各个部门的工作，提高工作效率。\n5.维修管理功能：修管理功能主要是对物业或设施的维修进行管理，包括维修任务的分配、维修进度的跟踪、维修费用的记录等。这个功能可以帮助管理者更好地安排和监控维修工作，保证物业或设施的正常运行。\n', '13111111111', '2024-02-18 16:45:04', 'http://localhost:8089/images/c054af8e-f85c-41cb-b5b1-3a30194b733c.jpg', '0');
INSERT INTO `paas_repair` VALUES (3, 6, 10, '标题6个字！', '爱上大时代', '1.采用前后端分离技术。前端使用vue为整个前端系统的脚手架。后台框架使用spring boot。\n4.部门管理功能：部门管理功能主要是对公司的各个部门进行管理，包括部门的名称、部门职责、部门人员等信息。这个功能可以帮助管理者更好地组织和管理公司内部各个部门的工作，提高工作效率。\n5.维修管理功能：修管理功能主要是对物业或设施的维修进行管理，包括维修任务的分配、维修进度的跟踪、维修费用的记录等。这个功能可以帮助管理者更好地安排和监控维修工作，保证物业或设施的正常运行。\n', '1111111', '2024-02-18 17:32:18', NULL, '2');
INSERT INTO `paas_repair` VALUES (4, 2, 10, '厕所爆炸了', '123', '123', '13111111111', '2024-02-19 16:32:46', NULL, '2');
INSERT INTO `paas_repair` VALUES (14, 2, 10, '厕所又爆炸了', 'A栋1单元401', '123', '13111111111', '2024-02-26 16:31:20', 'http://localhost:8089/images/92ffc111-0845-4ce4-bfa6-64096f1de719.jpg,http://localhost:8089/images/3e7bb651-ecc0-45c7-8927-f27bd80f947a.jpg', '2');
INSERT INTO `paas_repair` VALUES (15, 2, 10, '厕所又又爆炸了', 'A栋1单元401', '这是第三次爆炸了，你们厕所是不是定时炸弹啊', '13111111111', '2024-02-26 17:05:40', 'http://localhost:8089/images/3293162e-8375-41b7-8dea-28619e8614fb.jpg', '2');
INSERT INTO `paas_repair` VALUES (16, 2, NULL, '123', '123', '123', '13111111111', '2024-05-18 01:09:57', NULL, '0');

-- ----------------------------
-- Table structure for rental_customer
-- ----------------------------
DROP TABLE IF EXISTS `rental_customer`;
CREATE TABLE `rental_customer`  (
  `customer_id` int NOT NULL AUTO_INCREMENT COMMENT '租户Id',
  `cname` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '租户姓名',
  `cphone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '租户电话',
  `id_card` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '租户身份证',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别 0女 1男',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `del_flag` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '删除标志 0未删除 1已删除',
  `is_account_non_expired` int NULL DEFAULT NULL COMMENT '帐户是否过期 1未过期 0已过期',
  `is_account_non_locked` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '帐户是否被锁定 1未过期 0已过期',
  `is_credentials_non_expired` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码是否过期 1未过期 0已过期',
  `is_enabled` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '帐户是否可用 1可用 0删除用户',
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '租户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rental_customer
-- ----------------------------
INSERT INTO `rental_customer` VALUES (2, '中国移动', '13659630000', '444444197001010000', '0', '10086', '$2a$10$BLfpKjNkVQPuZE2TrN.OWeqChoiEwBAs4JpvKiWKPCaaBEWThvMDG', '0', 1, '1', '1', '1');
INSERT INTO `rental_customer` VALUES (6, '中国联通', '13659630001', '444444197001010000', '1', '10010', '$2a$10$290/0rUaRS2ClDh5Zh8YMeFiBE24dV6fN8Aqa0lQH2lTUqqUNN.D2', '0', 1, '1', '1', '1');
INSERT INTO `rental_customer` VALUES (7, '中国电信', '13659630002', '444444197001010000', '0', '10000', '$2a$10$S5gFxi9RSuU4mfWPqFu2vOUg8YhKqgPLuzW02g70pC1Er7PmQ2wRm', '0', 1, '1', '1', '1');

-- ----------------------------
-- Table structure for rental_customer_park
-- ----------------------------
DROP TABLE IF EXISTS `rental_customer_park`;
CREATE TABLE `rental_customer_park`  (
  `customer_park_id` int NOT NULL AUTO_INCREMENT COMMENT '租户车位Id',
  `rentalpark_num` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '租车位单号',
  `customer_id` int NULL DEFAULT NULL COMMENT '租户Id',
  `park_id` int NULL DEFAULT NULL COMMENT '车位Id',
  `month` int NULL DEFAULT NULL COMMENT '租凭月数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '起始时间',
  `status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '使用状态 0使用 1结束',
  PRIMARY KEY (`customer_park_id`) USING BTREE,
  INDEX `CPfk1`(`customer_id` ASC) USING BTREE,
  INDEX `CPfk2`(`park_id` ASC) USING BTREE,
  CONSTRAINT `CPfk1` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `CPfk2` FOREIGN KEY (`park_id`) REFERENCES `facility_park` (`park_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '租户车位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rental_customer_park
-- ----------------------------
INSERT INTO `rental_customer_park` VALUES (8, 'GYRP20240213042', 2, 3, 9, '2024-02-13 22:30:04', '0');
INSERT INTO `rental_customer_park` VALUES (9, 'GYRP20240227006', 6, 5, 7, '2024-02-27 00:00:00', '0');

-- ----------------------------
-- Table structure for rental_customer_role
-- ----------------------------
DROP TABLE IF EXISTS `rental_customer_role`;
CREATE TABLE `rental_customer_role`  (
  `customer_role_id` int NOT NULL AUTO_INCREMENT COMMENT '租户角色Id',
  `customer_id` int NULL DEFAULT NULL COMMENT '租户Id',
  `role_id` int NULL DEFAULT 2 COMMENT '角色Id',
  PRIMARY KEY (`customer_role_id`) USING BTREE,
  INDEX `CRfk1`(`customer_id` ASC) USING BTREE,
  INDEX `CRfk2`(`role_id` ASC) USING BTREE,
  CONSTRAINT `CRfk1` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `CRfk2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '租户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rental_customer_role
-- ----------------------------
INSERT INTO `rental_customer_role` VALUES (1, 2, 2);
INSERT INTO `rental_customer_role` VALUES (5, 6, 2);
INSERT INTO `rental_customer_role` VALUES (6, 7, 2);

-- ----------------------------
-- Table structure for rental_order
-- ----------------------------
DROP TABLE IF EXISTS `rental_order`;
CREATE TABLE `rental_order`  (
  `order_id` int NOT NULL AUTO_INCREMENT COMMENT '租单Id',
  `order_num` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '租单单号',
  `customer_id` int NULL DEFAULT NULL COMMENT '租户Id',
  `home_id` int NULL DEFAULT NULL COMMENT '房屋Id',
  `month` int NULL DEFAULT NULL COMMENT '租凭月数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `order_status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '租用状态 0在期 1结束',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `OCfk1`(`customer_id` ASC) USING BTREE,
  INDEX `OHfk2`(`home_id` ASC) USING BTREE,
  CONSTRAINT `OCfk1` FOREIGN KEY (`customer_id`) REFERENCES `rental_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `OHfk2` FOREIGN KEY (`home_id`) REFERENCES `house_home` (`home_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '租单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rental_order
-- ----------------------------
INSERT INTO `rental_order` VALUES (20, 'GYRO20240213012', 2, 1, 3, '2024-02-13 15:52:01', '0');
INSERT INTO `rental_order` VALUES (21, 'GYRO20240213196', 6, 2, 3, '2024-02-13 15:52:19', '0');
INSERT INTO `rental_order` VALUES (22, 'GYRO20240213542', 2, 4, 5, '2024-02-13 15:52:54', '0');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `parent_id` int NULL DEFAULT NULL COMMENT '父级菜单id',
  `menu_label` varchar(56) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `menu_code` varchar(56) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '权限字段',
  `name` varchar(56) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '路由名称',
  `path` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `url` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '组件路由',
  `type` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '类型 0目录 1菜单 2按钮',
  `icon` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图标',
  `order_num` int NULL DEFAULT NULL COMMENT '序号',
  `remark` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `parent_name` varchar(56) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '上级菜单名称',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', 'sys:system:index', '', '/system', '', '0', 'el-icon-user-solid', 1, '', '顶级菜单');
INSERT INTO `sys_menu` VALUES (2, 1, '员工管理', 'sys:user', 'UserList', 'UserList', '/system/UserList', '1', 'el-icon-user', 1, '', '系统管理');
INSERT INTO `sys_menu` VALUES (3, 1, '部门管理', 'sys:role', 'RoleList', 'RoleList', '/system/RoleList', '1', 'el-icon-user', 2, '', '系统管理');
INSERT INTO `sys_menu` VALUES (5, 1, '权限管理', 'sys:menu', 'MenuList', 'MenuList', '/system/MenuList', '1', 'el-icon-user', 3, '', '系统管理');
INSERT INTO `sys_menu` VALUES (6, 2, '新增', 'sys:user:add', '', '', '', '2', '', 1, '', '员工管理');
INSERT INTO `sys_menu` VALUES (7, 2, '编辑', 'sys:user:edit', '', '', '', '2', '', 2, '', '员工管理');
INSERT INTO `sys_menu` VALUES (8, 2, '删除', 'sys:user:delete', '', '', '', '2', '', 3, '', '员工管理');
INSERT INTO `sys_menu` VALUES (9, 3, '新增', 'sys:role:add', '', '', '', '2', '', 1, '', '角色管理');
INSERT INTO `sys_menu` VALUES (10, 3, '编辑', 'sys:role:edit', '', '', '', '2', '', 2, '', '角色管理');
INSERT INTO `sys_menu` VALUES (11, 3, '删除', 'sys:role:delete', '', '', '', '2', '', 3, '', '角色管理');
INSERT INTO `sys_menu` VALUES (12, 5, '新增', 'sys:menu:add', '', '', '', '2', '', 1, '', '权限管理');
INSERT INTO `sys_menu` VALUES (13, 5, '编辑', 'sys:menu:edit', '', '', '', '2', '', 2, '', '权限管理');
INSERT INTO `sys_menu` VALUES (14, 5, '删除', 'sys:menu:delete', '', '', '', '2', '', 3, '', '权限管理');
INSERT INTO `sys_menu` VALUES (15, 0, '房屋管理', 'wy:house:index', '', '/house', '', '0', 'el-icon-office-building', 2, '', '顶级菜单');
INSERT INTO `sys_menu` VALUES (16, 15, '房屋列表', 'wy:home', 'Homelist', 'Homelist', '/house/HomeList', '1', 'el-icon-s-home', 3, '', '房屋管理');
INSERT INTO `sys_menu` VALUES (17, 16, '新增', 'wy:home:add', '', '', '', '2', '', 1, '', '房屋列表');
INSERT INTO `sys_menu` VALUES (18, 16, '编辑', 'wy:home:edit', '', '', '', '2', '', 2, '', '房屋列表');
INSERT INTO `sys_menu` VALUES (19, 16, '删除', 'wy:home:delete', '', '', '', '2', '', 3, '', '房屋列表');
INSERT INTO `sys_menu` VALUES (22, 15, '单元列表', 'wy:unit', 'UnitList', 'UnitList', '/house/UnitList', '1', 'el-icon-s-home', 2, '', '房屋管理');
INSERT INTO `sys_menu` VALUES (23, 15, '楼栋列表', 'wy:building', 'BuildingList', 'BuildingList', '/house/BuildingList', '1', 'el-icon-s-home', 1, '', '房屋管理');
INSERT INTO `sys_menu` VALUES (24, 23, '新增', 'wy:building:add', '', '', '', '2', '', 1, '', '楼栋列表');
INSERT INTO `sys_menu` VALUES (25, 23, '编辑', 'wy:building:edit', '', '', '', '2', '', 2, '', '楼栋列表');
INSERT INTO `sys_menu` VALUES (26, 23, '删除', 'wy:building:delete', '', '', '', '2', '', 3, '', '楼栋列表');
INSERT INTO `sys_menu` VALUES (27, 22, '新增', 'wy:unit:add', '', '', '', '2', '', 1, '', '单元列表');
INSERT INTO `sys_menu` VALUES (28, 22, '编辑', 'wy:unit:edit', '', '', '', '2', '', 2, '', '单元列表');
INSERT INTO `sys_menu` VALUES (29, 22, '删除', 'wy:unit:delete', '', '', '', '2', '', 3, '', '单元列表');
INSERT INTO `sys_menu` VALUES (30, 0, '设施管理', 'wy:facility:index', '', '/facility', '', '0', 'el-icon-house', 3, '', '顶级菜单');
INSERT INTO `sys_menu` VALUES (31, 30, '车位管理', 'wy:park', 'ParkList', 'ParkList', '/facility/ParkList', '1', 'el-icon-truck', 1, '', '设施管理');
INSERT INTO `sys_menu` VALUES (32, 31, '新增', 'wy:park:add', '', '', '', '2', '', 1, '', '车位管理');
INSERT INTO `sys_menu` VALUES (33, 31, '编辑', 'wy:park:edit', '', '', '', '2', '', 2, '', '车位管理');
INSERT INTO `sys_menu` VALUES (34, 31, '删除', 'wy:park:delete', '', '', '', '2', '', 3, '', '车位管理');
INSERT INTO `sys_menu` VALUES (35, 0, '租凭管理', 'wy:rental:index', '', '/rental', '', '0', 'el-icon-s-order', 4, '', '顶级菜单');
INSERT INTO `sys_menu` VALUES (36, 0, '收费管理', 'wy:fee:index', '', '/fee', '', '0', 'el-icon-money', 5, '', '顶级菜单');
INSERT INTO `sys_menu` VALUES (37, 0, '平台服务', 'wy:pass:index', '', '/paas', '', '0', 'el-icon-s-comment', 7, '', '顶级菜单');
INSERT INTO `sys_menu` VALUES (38, 35, '租户管理', 'wy:customer', 'CustomerList', 'CustomerList', '/rental/CustomerList', '1', 'el-icon-s-custom', 1, '', '租凭管理');
INSERT INTO `sys_menu` VALUES (39, 35, '租房单管理', 'wy:order', 'OrderList', 'OrderList', '/rental/OrderList', '1', 'el-icon-s-claim', 2, '', '租凭管理');
INSERT INTO `sys_menu` VALUES (40, 35, '租车位管理', 'wy:rentalpark', 'RentalParkList', 'RentalParkList', '/rental/RentalParkList', '1', 'el-icon-truck', 3, '', '租凭管理');
INSERT INTO `sys_menu` VALUES (41, 36, '电费管理', 'wy:powerfee', 'PowerFeeList', 'PowerFeeList', '/fee/PowerFeeList', '1', 'el-icon-odometer', 1, '', '收费管理');
INSERT INTO `sys_menu` VALUES (42, 36, '水费管理', 'wy:waterfee', 'WaterFeeList', 'WaterFeeList', '/fee/WaterFeeList', '1', 'el-icon-odometer', 2, '', '收费管理');
INSERT INTO `sys_menu` VALUES (43, 36, '租凭费管理', 'wy:rentalfee', 'RentalFeeList', 'RentalFeeList', '/fee/RentalFeeList', '1', 'el-icon-s-claim', 3, '', '收费管理');
INSERT INTO `sys_menu` VALUES (44, 36, '停车费管理', 'wy:parkfee', 'ParkFeeList', 'ParkFeeList', '/fee/ParkFeeList', '1', 'el-icon-truck', 4, '', '收费管理');
INSERT INTO `sys_menu` VALUES (45, 38, '新增', 'wy:customer:add', '', '', '', '2', '', 1, '', '租户管理');
INSERT INTO `sys_menu` VALUES (46, 38, '编辑', 'wy:customer:edit', '', '', '', '2', '', 2, '', '租户管理');
INSERT INTO `sys_menu` VALUES (47, 38, '删除', 'wy:customer:delete', '', '', '', '2', '', 3, '', '租户管理');
INSERT INTO `sys_menu` VALUES (48, 39, '新增', 'wy:order:add', '', '', '', '2', '', 1, '', '租房单管理');
INSERT INTO `sys_menu` VALUES (49, 39, '编辑', 'wy:order:edit', '', '', '', '2', '', 2, '', '租房单管理');
INSERT INTO `sys_menu` VALUES (50, 39, '删除', 'wy:order:delete', '', '', '', '2', '', 3, '', '租房单管理');
INSERT INTO `sys_menu` VALUES (51, 40, '新增', 'wy:rentalpark:add', '', '', '', '2', '', 1, '', '租车位管理');
INSERT INTO `sys_menu` VALUES (52, 40, '编辑', 'wy:rentalpark:edit', '', '', '', '2', '', 2, '', '租车位管理');
INSERT INTO `sys_menu` VALUES (53, 40, '删除', 'wy:rentalpark:delete', '', '', '', '2', '', 3, '', '租车位管理');
INSERT INTO `sys_menu` VALUES (54, 41, '新增', 'wy:powerfee:add', '', '', '', '2', '', 1, '', '电费管理');
INSERT INTO `sys_menu` VALUES (55, 41, '编辑', 'wy:powerfee:edit', '', '', '', '2', '', 2, '', '电费管理');
INSERT INTO `sys_menu` VALUES (56, 41, '删除', 'wy:powerfee:delete', '', '', '', '2', '', 3, '', '电费管理');
INSERT INTO `sys_menu` VALUES (57, 90, '缴费', 'wy:powerfee:pay', '', '', '', '2', '', 4, '', '我的电费');
INSERT INTO `sys_menu` VALUES (58, 39, '退房', 'wy:order:return', '', '', '', '2', '', 4, '', '租房单管理');
INSERT INTO `sys_menu` VALUES (59, 40, '退车位', 'wy:rentalpark:return', '', '', '', '2', '', 4, '', '租车位管理');
INSERT INTO `sys_menu` VALUES (60, 42, '新增', 'wy:waterfee:add', '', '', '', '2', '', 1, '', '水费管理');
INSERT INTO `sys_menu` VALUES (61, 42, '编辑', 'wy:waterfee:edit', '', '', '', '2', '', 2, '', '水费管理');
INSERT INTO `sys_menu` VALUES (62, 42, '删除', 'wy:waterfee:delete', '', '', '', '2', '', 3, '', '水费管理');
INSERT INTO `sys_menu` VALUES (63, 91, '缴费', 'wy:waterfee:pay', '', '', '', '2', '', 4, '', '我的水费');
INSERT INTO `sys_menu` VALUES (64, 43, '新增', 'wy:rentalfee:add', '', '', '', '2', '', 1, '', '租凭费管理');
INSERT INTO `sys_menu` VALUES (65, 43, '编辑', 'wy:rentalfee:edit', '', '', '', '2', '', 2, '', '租凭费管理');
INSERT INTO `sys_menu` VALUES (66, 43, '删除', 'wy:rentalfee:delete', '', '', '', '2', '', 3, '', '租凭费管理');
INSERT INTO `sys_menu` VALUES (67, 92, '缴费', 'wy:rentalfee:pay', '', '', '', '2', '', 4, '', '我的租凭费');
INSERT INTO `sys_menu` VALUES (68, 44, '新增', 'wy:parkfee:add', '', '', '', '2', '', 1, '', '停车费管理');
INSERT INTO `sys_menu` VALUES (69, 44, '编辑', 'wy:parkfee:edit', '', '', '', '2', '', 2, '', '停车费管理');
INSERT INTO `sys_menu` VALUES (70, 44, '删除', 'wy:parkfee:delete', '', '', '', '2', '', 3, '', '停车费管理');
INSERT INTO `sys_menu` VALUES (71, 94, '缴费', 'wy:parkfee:pay', '', '', '', '2', '', 4, '', '我的停车费');
INSERT INTO `sys_menu` VALUES (72, 2, '分配角色', 'sys:user:saverole', '', '', '', '2', '', 4, '', '员工管理');
INSERT INTO `sys_menu` VALUES (73, 3, '分配权限', 'sys:role:saveassign', '', '', '', '2', '', 4, '', '角色管理');
INSERT INTO `sys_menu` VALUES (74, 37, '投诉管理', 'wy:complaint', 'ComplaintList', 'ComplaintList', '/paas/ComplaintList', '1', 'el-icon-phone-outline', 1, '', '平台服务');
INSERT INTO `sys_menu` VALUES (75, 37, '我的投诉', 'wy:mycomplaint', 'MyComplaintList', 'MyComplaintList', '/paas/MyComplaintList', '1', 'el-icon-phone-outline', 2, '', '平台服务');
INSERT INTO `sys_menu` VALUES (76, 37, '维修管理', 'wy:repair', 'RepairList', 'RepairList', '/paas/RepairList', '1', 'el-icon-paperclip', 3, '', '平台服务');
INSERT INTO `sys_menu` VALUES (77, 37, '我的维修', 'wy:myrepair', 'MyRepairList', 'MyRepairList', '/paas/MyRepairList', '1', 'el-icon-paperclip', 5, '', '平台服务');
INSERT INTO `sys_menu` VALUES (78, 37, '公告管理', 'wy:notice', 'NoticeList', 'NoticeList', '/paas/NoticeList', '1', 'el-icon-bell', 6, '', '平台服务');
INSERT INTO `sys_menu` VALUES (79, 74, '处理', 'wy:complaint:do', '', '', '', '2', '', 1, '', '投诉管理');
INSERT INTO `sys_menu` VALUES (80, 75, '新增', 'wy:mycomplaint:add', '', '', '', '2', '', 1, '', '我的投诉');
INSERT INTO `sys_menu` VALUES (81, 75, '编辑', 'wy:mycomplaint:edit', '', '', '', '2', '', 2, '', '我的投诉');
INSERT INTO `sys_menu` VALUES (82, 75, '删除', 'wy:mycomplaint:delete', '', '', '', '2', '', 3, '', '我的投诉');
INSERT INTO `sys_menu` VALUES (83, 76, '派修', 'wy:repair:assign', '', '', '', '2', '', 1, '', '维修管理');
INSERT INTO `sys_menu` VALUES (84, 77, '新增', 'wy:myrepair:add', '', '', '', '2', '', 1, '', '我的维修');
INSERT INTO `sys_menu` VALUES (85, 77, '编辑', 'wy:myrepair:edit', '', '', '', '2', '', 2, '', '我的维修');
INSERT INTO `sys_menu` VALUES (86, 77, '删除', 'wy:myrepair:delete', '', '', '', '2', '', 3, '', '我的维修');
INSERT INTO `sys_menu` VALUES (87, 78, '新增', 'wy:notice:add', '', '', '', '2', '', 1, '', '公告管理');
INSERT INTO `sys_menu` VALUES (88, 78, '编辑', 'wy:notice:edit', '', '', '', '2', '', 2, '', '公告管理');
INSERT INTO `sys_menu` VALUES (89, 78, '删除', 'wy:notice:delete', '', '', '', '2', '', 3, '', '公告管理');
INSERT INTO `sys_menu` VALUES (90, 93, '我的电费', 'wy:mypowerfee', 'MyPowerFeeList', 'MyPowerFeeList', '/fee/MyPowerFeeList', '1', '', 1, '', '我的缴费');
INSERT INTO `sys_menu` VALUES (91, 93, '我的水费', 'wy:mywaterfee', 'MyWaterFeeList', 'MyWaterFeeList', '/fee/MyWaterFeeList', '1', '', 2, '', '我的缴费');
INSERT INTO `sys_menu` VALUES (92, 93, '我的租凭费', 'wy:myrentalfee', 'MyRentalFeeList', 'MyRentalFeeList', '/fee/MyRentalFeeList', '1', '', 3, '', '我的缴费');
INSERT INTO `sys_menu` VALUES (93, 0, '我的缴费', 'wy:myfee', '', '/myfee', '', '0', 'el-icon-money', 6, '', '顶级菜单');
INSERT INTO `sys_menu` VALUES (94, 93, '我的停车费', 'wy:myparkfee', 'MyParkFeeList', 'MyParkFeeList', '/fee/MyParkFeeList', '1', '', 4, '', '我的缴费');
INSERT INTO `sys_menu` VALUES (95, 37, '待处理维修', 'wy:dorepair', 'DoRepairList', 'DoRepairList', '/paas/DoRepairList', '1', 'el-icon-paperclip', 4, '', '平台服务');
INSERT INTO `sys_menu` VALUES (96, 95, '处理', 'wy:dorepair:do', '', '', '', '2', '', 1, '', '待处理维修');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '部门角色id',
  `role_name` varchar(56) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `remark` varchar(56) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (2, '业主', '负责分配业主的功能权限');
INSERT INTO `sys_role` VALUES (3, '系统管理部门', '负责管理系统的各种功能');
INSERT INTO `sys_role` VALUES (5, '物业管理部门', '负责管理小区物业业务的部门');
INSERT INTO `sys_role` VALUES (7, '维修部门', '负责维修小区报修的部门');
INSERT INTO `sys_role` VALUES (9, '测试部门', 'test');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_menu_id` int NOT NULL AUTO_INCREMENT COMMENT '部门菜单id',
  `role_id` int NULL DEFAULT NULL COMMENT '部门角色id',
  `menu_id` int NULL DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`role_menu_id`) USING BTREE,
  INDEX `RMfk1`(`role_id` ASC) USING BTREE,
  INDEX `RMfk2`(`menu_id` ASC) USING BTREE,
  CONSTRAINT `RMfk1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `RMfk2` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`menu_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1181 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (930, 2, 93);
INSERT INTO `sys_role_menu` VALUES (931, 2, 90);
INSERT INTO `sys_role_menu` VALUES (932, 2, 57);
INSERT INTO `sys_role_menu` VALUES (933, 2, 91);
INSERT INTO `sys_role_menu` VALUES (934, 2, 63);
INSERT INTO `sys_role_menu` VALUES (935, 2, 92);
INSERT INTO `sys_role_menu` VALUES (936, 2, 67);
INSERT INTO `sys_role_menu` VALUES (937, 2, 94);
INSERT INTO `sys_role_menu` VALUES (938, 2, 71);
INSERT INTO `sys_role_menu` VALUES (939, 2, 75);
INSERT INTO `sys_role_menu` VALUES (940, 2, 80);
INSERT INTO `sys_role_menu` VALUES (941, 2, 81);
INSERT INTO `sys_role_menu` VALUES (942, 2, 82);
INSERT INTO `sys_role_menu` VALUES (943, 2, 77);
INSERT INTO `sys_role_menu` VALUES (944, 2, 84);
INSERT INTO `sys_role_menu` VALUES (945, 2, 85);
INSERT INTO `sys_role_menu` VALUES (946, 2, 86);
INSERT INTO `sys_role_menu` VALUES (947, 2, 37);
INSERT INTO `sys_role_menu` VALUES (948, 5, 15);
INSERT INTO `sys_role_menu` VALUES (949, 5, 23);
INSERT INTO `sys_role_menu` VALUES (950, 5, 24);
INSERT INTO `sys_role_menu` VALUES (951, 5, 25);
INSERT INTO `sys_role_menu` VALUES (952, 5, 26);
INSERT INTO `sys_role_menu` VALUES (953, 5, 22);
INSERT INTO `sys_role_menu` VALUES (954, 5, 27);
INSERT INTO `sys_role_menu` VALUES (955, 5, 28);
INSERT INTO `sys_role_menu` VALUES (956, 5, 29);
INSERT INTO `sys_role_menu` VALUES (957, 5, 16);
INSERT INTO `sys_role_menu` VALUES (958, 5, 17);
INSERT INTO `sys_role_menu` VALUES (959, 5, 18);
INSERT INTO `sys_role_menu` VALUES (960, 5, 19);
INSERT INTO `sys_role_menu` VALUES (961, 5, 30);
INSERT INTO `sys_role_menu` VALUES (962, 5, 31);
INSERT INTO `sys_role_menu` VALUES (963, 5, 32);
INSERT INTO `sys_role_menu` VALUES (964, 5, 33);
INSERT INTO `sys_role_menu` VALUES (965, 5, 34);
INSERT INTO `sys_role_menu` VALUES (966, 5, 35);
INSERT INTO `sys_role_menu` VALUES (967, 5, 38);
INSERT INTO `sys_role_menu` VALUES (968, 5, 45);
INSERT INTO `sys_role_menu` VALUES (969, 5, 46);
INSERT INTO `sys_role_menu` VALUES (970, 5, 47);
INSERT INTO `sys_role_menu` VALUES (971, 5, 39);
INSERT INTO `sys_role_menu` VALUES (972, 5, 48);
INSERT INTO `sys_role_menu` VALUES (973, 5, 49);
INSERT INTO `sys_role_menu` VALUES (974, 5, 50);
INSERT INTO `sys_role_menu` VALUES (975, 5, 58);
INSERT INTO `sys_role_menu` VALUES (976, 5, 40);
INSERT INTO `sys_role_menu` VALUES (977, 5, 51);
INSERT INTO `sys_role_menu` VALUES (978, 5, 52);
INSERT INTO `sys_role_menu` VALUES (979, 5, 53);
INSERT INTO `sys_role_menu` VALUES (980, 5, 59);
INSERT INTO `sys_role_menu` VALUES (981, 5, 36);
INSERT INTO `sys_role_menu` VALUES (982, 5, 41);
INSERT INTO `sys_role_menu` VALUES (983, 5, 54);
INSERT INTO `sys_role_menu` VALUES (984, 5, 55);
INSERT INTO `sys_role_menu` VALUES (985, 5, 56);
INSERT INTO `sys_role_menu` VALUES (986, 5, 42);
INSERT INTO `sys_role_menu` VALUES (987, 5, 60);
INSERT INTO `sys_role_menu` VALUES (988, 5, 61);
INSERT INTO `sys_role_menu` VALUES (989, 5, 62);
INSERT INTO `sys_role_menu` VALUES (990, 5, 43);
INSERT INTO `sys_role_menu` VALUES (991, 5, 64);
INSERT INTO `sys_role_menu` VALUES (992, 5, 65);
INSERT INTO `sys_role_menu` VALUES (993, 5, 66);
INSERT INTO `sys_role_menu` VALUES (994, 5, 44);
INSERT INTO `sys_role_menu` VALUES (995, 5, 68);
INSERT INTO `sys_role_menu` VALUES (996, 5, 69);
INSERT INTO `sys_role_menu` VALUES (997, 5, 70);
INSERT INTO `sys_role_menu` VALUES (998, 5, 74);
INSERT INTO `sys_role_menu` VALUES (999, 5, 79);
INSERT INTO `sys_role_menu` VALUES (1000, 5, 76);
INSERT INTO `sys_role_menu` VALUES (1001, 5, 83);
INSERT INTO `sys_role_menu` VALUES (1002, 5, 78);
INSERT INTO `sys_role_menu` VALUES (1003, 5, 87);
INSERT INTO `sys_role_menu` VALUES (1004, 5, 88);
INSERT INTO `sys_role_menu` VALUES (1005, 5, 89);
INSERT INTO `sys_role_menu` VALUES (1006, 5, 37);
INSERT INTO `sys_role_menu` VALUES (1028, 7, 95);
INSERT INTO `sys_role_menu` VALUES (1029, 7, 96);
INSERT INTO `sys_role_menu` VALUES (1030, 7, 37);
INSERT INTO `sys_role_menu` VALUES (1107, 3, 1);
INSERT INTO `sys_role_menu` VALUES (1108, 3, 2);
INSERT INTO `sys_role_menu` VALUES (1109, 3, 6);
INSERT INTO `sys_role_menu` VALUES (1110, 3, 7);
INSERT INTO `sys_role_menu` VALUES (1111, 3, 8);
INSERT INTO `sys_role_menu` VALUES (1112, 3, 72);
INSERT INTO `sys_role_menu` VALUES (1113, 3, 3);
INSERT INTO `sys_role_menu` VALUES (1114, 3, 9);
INSERT INTO `sys_role_menu` VALUES (1115, 3, 10);
INSERT INTO `sys_role_menu` VALUES (1116, 3, 11);
INSERT INTO `sys_role_menu` VALUES (1117, 3, 73);
INSERT INTO `sys_role_menu` VALUES (1118, 3, 5);
INSERT INTO `sys_role_menu` VALUES (1119, 3, 12);
INSERT INTO `sys_role_menu` VALUES (1120, 3, 13);
INSERT INTO `sys_role_menu` VALUES (1121, 3, 14);
INSERT INTO `sys_role_menu` VALUES (1122, 3, 15);
INSERT INTO `sys_role_menu` VALUES (1123, 3, 23);
INSERT INTO `sys_role_menu` VALUES (1124, 3, 24);
INSERT INTO `sys_role_menu` VALUES (1125, 3, 25);
INSERT INTO `sys_role_menu` VALUES (1126, 3, 26);
INSERT INTO `sys_role_menu` VALUES (1127, 3, 22);
INSERT INTO `sys_role_menu` VALUES (1128, 3, 27);
INSERT INTO `sys_role_menu` VALUES (1129, 3, 28);
INSERT INTO `sys_role_menu` VALUES (1130, 3, 29);
INSERT INTO `sys_role_menu` VALUES (1131, 3, 16);
INSERT INTO `sys_role_menu` VALUES (1132, 3, 17);
INSERT INTO `sys_role_menu` VALUES (1133, 3, 18);
INSERT INTO `sys_role_menu` VALUES (1134, 3, 19);
INSERT INTO `sys_role_menu` VALUES (1135, 3, 30);
INSERT INTO `sys_role_menu` VALUES (1136, 3, 31);
INSERT INTO `sys_role_menu` VALUES (1137, 3, 32);
INSERT INTO `sys_role_menu` VALUES (1138, 3, 33);
INSERT INTO `sys_role_menu` VALUES (1139, 3, 34);
INSERT INTO `sys_role_menu` VALUES (1140, 3, 35);
INSERT INTO `sys_role_menu` VALUES (1141, 3, 38);
INSERT INTO `sys_role_menu` VALUES (1142, 3, 45);
INSERT INTO `sys_role_menu` VALUES (1143, 3, 46);
INSERT INTO `sys_role_menu` VALUES (1144, 3, 47);
INSERT INTO `sys_role_menu` VALUES (1145, 3, 39);
INSERT INTO `sys_role_menu` VALUES (1146, 3, 48);
INSERT INTO `sys_role_menu` VALUES (1147, 3, 49);
INSERT INTO `sys_role_menu` VALUES (1148, 3, 50);
INSERT INTO `sys_role_menu` VALUES (1149, 3, 58);
INSERT INTO `sys_role_menu` VALUES (1150, 3, 40);
INSERT INTO `sys_role_menu` VALUES (1151, 3, 51);
INSERT INTO `sys_role_menu` VALUES (1152, 3, 52);
INSERT INTO `sys_role_menu` VALUES (1153, 3, 53);
INSERT INTO `sys_role_menu` VALUES (1154, 3, 59);
INSERT INTO `sys_role_menu` VALUES (1155, 3, 36);
INSERT INTO `sys_role_menu` VALUES (1156, 3, 41);
INSERT INTO `sys_role_menu` VALUES (1157, 3, 54);
INSERT INTO `sys_role_menu` VALUES (1158, 3, 55);
INSERT INTO `sys_role_menu` VALUES (1159, 3, 56);
INSERT INTO `sys_role_menu` VALUES (1160, 3, 42);
INSERT INTO `sys_role_menu` VALUES (1161, 3, 60);
INSERT INTO `sys_role_menu` VALUES (1162, 3, 61);
INSERT INTO `sys_role_menu` VALUES (1163, 3, 62);
INSERT INTO `sys_role_menu` VALUES (1164, 3, 43);
INSERT INTO `sys_role_menu` VALUES (1165, 3, 64);
INSERT INTO `sys_role_menu` VALUES (1166, 3, 65);
INSERT INTO `sys_role_menu` VALUES (1167, 3, 66);
INSERT INTO `sys_role_menu` VALUES (1168, 3, 44);
INSERT INTO `sys_role_menu` VALUES (1169, 3, 68);
INSERT INTO `sys_role_menu` VALUES (1170, 3, 69);
INSERT INTO `sys_role_menu` VALUES (1171, 3, 70);
INSERT INTO `sys_role_menu` VALUES (1172, 3, 74);
INSERT INTO `sys_role_menu` VALUES (1173, 3, 79);
INSERT INTO `sys_role_menu` VALUES (1174, 3, 76);
INSERT INTO `sys_role_menu` VALUES (1175, 3, 83);
INSERT INTO `sys_role_menu` VALUES (1176, 3, 78);
INSERT INTO `sys_role_menu` VALUES (1177, 3, 87);
INSERT INTO `sys_role_menu` VALUES (1178, 3, 88);
INSERT INTO `sys_role_menu` VALUES (1179, 3, 89);
INSERT INTO `sys_role_menu` VALUES (1180, 3, 37);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `uname` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `uphone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电话',
  `sex` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '性别 0女 1男',
  `id_card` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `status` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否在职 0在职 1离职',
  `is_admin` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '是否管理员 0否 1是',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账户',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `del_flag` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '删除标志 0未删除 1已删除',
  `is_account_non_expired` int NULL DEFAULT NULL COMMENT '帐户是否过期 1未过期 0已过期',
  `is_account_non_locked` int NULL DEFAULT NULL COMMENT '帐户是否被锁定 1未过期 0已过期',
  `is_credentials_non_expired` int NULL DEFAULT NULL COMMENT '密码是否过期 1未过期 0已过期',
  `is_enabled` int NULL DEFAULT NULL COMMENT '帐户是否可用 1可用 0删除用户',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '物业用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '系统管理员001', '13659630003', '1', '444444197001010000', '0', '1', 'admin', '$2a$10$isI6zvhjCfadTsJHuhy5p.03LOS0KB8kYzf3ju/W/NWA.M4Kd0TTC', '0', 1, 1, 1, 1);
INSERT INTO `sys_user` VALUES (10, '张三', '13659630004', '1', '444444197001010000', '0', '0', 'user', '$2a$10$712SdryFvklEWJphilSitu9ZSVItwbk8EIZMwenktyKfh4eq8d4JS', '0', 1, 1, 1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_role_id` int NOT NULL AUTO_INCREMENT COMMENT '用户角色id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`user_role_id`) USING BTREE,
  INDEX `URfk1`(`role_id` ASC) USING BTREE,
  INDEX `URfk2`(`user_id` ASC) USING BTREE,
  CONSTRAINT `URfk1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `URfk2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (16, 7, 10);
INSERT INTO `sys_user_role` VALUES (17, 3, 1);

SET FOREIGN_KEY_CHECKS = 1;
