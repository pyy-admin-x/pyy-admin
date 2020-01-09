/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : pyy_sys_base

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 03/01/2020 17:39:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_datasource
-- ----------------------------
DROP TABLE IF EXISTS `sys_datasource`;
CREATE TABLE `sys_datasource`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '名称',
  `db_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据库类型',
  `host` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '服务器地址',
  `port` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '端口号',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '密码',
  `db_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据库名称',
  `create_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人ID',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人ID',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `deleted_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标记: 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '数据源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_datasource
-- ----------------------------
INSERT INTO `sys_datasource` VALUES ('1211499098994237442', '基础服务', 'MySQL', 'localhost', '3306', 'root', '123456', 'pyy_sys_base', 'root', 'root', '2019-12-30 12:07:48', NULL, NULL, '0000-00-00 00:00:00', 0);
INSERT INTO `sys_datasource` VALUES ('1211551270360444929', '12565', 'MySQL', '12', '12', '12', '12', '123', 'root', 'root', '2019-12-30 15:35:07', 'root', 'root', '2019-12-31 15:38:02', 0);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '部门ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '部门名称',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '上级部门ID',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `create_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人ID',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人ID',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `deleted_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标记: 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1212621474091806722', '大数据产业中心', NULL, 1, '大数据产业中心', 'root', 'root', '2020-01-02 06:27:43', NULL, NULL, NULL, 0);
INSERT INTO `sys_dept` VALUES ('1212621575099035650', '项目一部', '1212621474091806722', 1, '项目一部', 'root', 'root', '2020-01-02 06:28:08', NULL, NULL, NULL, 0);
INSERT INTO `sys_dept` VALUES ('1212621842032930817', '同方软件开发部', NULL, 1, '同方软件开发部', 'root', 'root', '2020-01-02 06:29:11', NULL, NULL, NULL, 0);
INSERT INTO `sys_dept` VALUES ('1212926120618377218', '研发部1', '1212621842032930817', 1, '', 'root', 'root', '2020-01-03 02:38:17', 'root', 'root', '2020-01-03 02:42:46', 1);
INSERT INTO `sys_dept` VALUES ('1212929383258406913', '项目二部', '1212621474091806722', 2, '项目二部', 'root', 'root', '2020-01-03 02:51:15', 'root', 'root', '2020-01-03 02:51:26', 0);

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '岗位ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '部门名称',
  `dept_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '部门ID',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `create_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人ID',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人ID',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `deleted_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标记: 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '岗位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES ('1213024662187958274', 'Java高级开发', '1212929383258406913', 1, '开发', 'root', 'root', '2020-01-03 09:09:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_job` VALUES ('1213024729418457090', '数据高级开发', '1212929383258406913', 2, '数据库', 'root', 'root', '2020-01-03 09:10:07', NULL, NULL, NULL, 0);
INSERT INTO `sys_job` VALUES ('1213024769784438785', 'GIS高级开发', '1212929383258406913', 3, '数据库', 'root', 'root', '2020-01-03 09:10:17', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `request_ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作IP',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '数据类型：1 操作记录 2 异常记录',
  `userId` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作人ID',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作人名称',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作描述',
  `action_method` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '请求目标方法',
  `action_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '请求url',
  `params` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '请求参数',
  `browser` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '浏览器',
  `class_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类路径',
  `request_method` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '请求方法',
  `operate_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作类型：1 查询/获取 2 添加 3 修改 4 删除',
  `start_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '开始时间',
  `finish_time` timestamp(0) NOT NULL COMMENT '完成时间',
  `consuming_time` bigint(20) NULL DEFAULT NULL COMMENT '消耗时间',
  `ex_detail` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '异常详情信息 堆栈信息',
  `ex_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '异常描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '操作日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('1211910733684572162', '127.0.0.1', 1, NULL, 'root', '修改数据源', 'edit', '/v1/dataSource/1211551270360444929', '[1211551270360444929, DataSource(id=1211551270360444929, name=12312312312123123, dbType=MySQL, host=12211212, port=1212123, username=1212123123, password=1212121231221, dbName=12123123)]', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36', 'com.thtf.generate.server.controller.DataSourceController', 'PUT', '1', '2019-12-31 07:23:21', '2019-12-31 07:23:21', 1, NULL, NULL);
INSERT INTO `sys_log` VALUES ('1211911436037537794', '127.0.0.1', 1, NULL, 'root', '修改数据源', 'edit', '/v1/dataSource/1211551270360444929', '[1211551270360444929, DataSource(id=1211551270360444929, name=1231, dbType=MySQL, host=12211212, port=1212123, username=1212123123, password=1212121231221, dbName=12123123)]', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36', 'com.thtf.generate.server.controller.DataSourceController', 'PUT', '1', '2019-12-31 07:26:12', '2019-12-31 07:26:12', 43, NULL, NULL);
INSERT INTO `sys_log` VALUES ('1211911899248185346', '127.0.0.1', 1, NULL, 'root', '修改数据源', 'edit', '/v1/dataSource/1211551270360444929', '[1211551270360444929, DataSource(id=1211551270360444929, name=11111, dbType=MySQL, host=12211212, port=1212123, username=1212123123, password=1212121231221, dbName=12123123)]', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36', 'com.thtf.generate.server.controller.DataSourceController', 'PUT', '1', '2019-12-31 07:28:06', '2019-12-31 07:28:06', 10, NULL, NULL);
INSERT INTO `sys_log` VALUES ('1211915134105124866', '127.0.0.1', 1, NULL, 'root', '添加数据源', 'add', '/v1/dataSource', '[DataSource(id=, name=12512, dbType=MySQL, host=123, port=123, username=14, password=123, dbName=12)]', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36', 'com.thtf.generate.server.controller.DataSourceController', 'POST', '2', '2019-12-31 07:40:57', '2019-12-31 07:40:57', 11, NULL, NULL);
INSERT INTO `sys_log` VALUES ('1211915874873737218', '127.0.0.1', 1, NULL, 'root', '删除数据源', 'delete', '/v1/dataSource/1211915127427715074', '[1211915127427715074]', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36', 'com.thtf.generate.server.controller.DataSourceController', 'DELETE', '4', '2019-12-31 07:43:54', '2019-12-31 07:43:54', 11, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '密码',
  `dept_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '部门ID',
  `job_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '岗位ID',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '头像',
  `status` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '状态：0-正常 1-锁定',
  `create_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人ID',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人ID',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人名称',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `deleted_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标记: 0-未删除 1-已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
