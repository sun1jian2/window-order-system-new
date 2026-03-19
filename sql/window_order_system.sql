/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 90500 (9.5.0)
 Source Host           : localhost:3306
 Source Schema         : window_order_system

 Target Server Type    : MySQL
 Target Server Version : 90500 (9.5.0)
 File Encoding         : 65001

 Date: 19/03/2026 12:22:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for after_sales_order
-- ----------------------------
DROP TABLE IF EXISTS `after_sales_order`;
CREATE TABLE `after_sales_order`
(
    `id`                bigint         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `ticket_no`         varchar(50)    NOT NULL COMMENT '工单编号',
    `order_id`          bigint         NOT NULL COMMENT '关联原订单ID',
    `customer_name`     varchar(50)    NULL DEFAULT NULL COMMENT '客户姓名',
    `customer_phone`    varchar(20)    NULL DEFAULT NULL COMMENT '联系电话',
    `address`           varchar(255)   NULL DEFAULT NULL COMMENT '上门地址',
    `issue_description` text           NULL COMMENT '问题描述',
    `status`            varchar(20)    NULL DEFAULT 'PENDING' COMMENT '状态：PENDING(待处理), ASSIGNED(已指派), PROCESSING(处理中), COMPLETED(已完成), CANCELLED(已取消)',
    `handler_id`        bigint         NULL DEFAULT NULL COMMENT '处理人ID（通常是安装师傅）',
    `appointment_time`  datetime       NULL DEFAULT NULL COMMENT '预约上门时间',
    `completion_time`   datetime       NULL DEFAULT NULL COMMENT '完成时间',
    `solution`          text           NULL COMMENT '解决方案/维修结果',
    `fee`               decimal(10, 2) NULL DEFAULT 0.00 COMMENT '维修费用',
    `create_by`         bigint         NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`       datetime       NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        tinyint(1)     NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_ticket_no` (`ticket_no` ASC) USING BTREE,
    INDEX `idx_order_id` (`order_id` ASC) USING BTREE,
    INDEX `idx_handler_id` (`handler_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '售后工单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(50)  NOT NULL COMMENT '品牌名称',
    `description` varchar(255) NULL DEFAULT NULL COMMENT '品牌描述',
    `create_by`   bigint       NULL DEFAULT NULL COMMENT '创建人ID',
    `update_by`   bigint       NULL DEFAULT NULL COMMENT '更新人ID',
    `is_deleted`  tinyint(1)   NULL DEFAULT 0 COMMENT '是否删除（0：否，1：是）',
    `create_time` datetime     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_name` (`name` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '窗户品牌表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(50)  NOT NULL COMMENT '客户姓名',
    `phone`       varchar(20)  NOT NULL COMMENT '联系电话',
    `address`     varchar(255) NULL DEFAULT NULL COMMENT '默认地址',
    `source`      VARCHAR(20)       DEFAULT 'ORDER' COMMENT '客户来源: ORDER-订单自动生成, MANUAL-手动添加',
    `remark`      varchar(500) NULL DEFAULT NULL COMMENT '备注（如：客户偏好、性格等）',
    `create_by`   bigint       NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time` datetime     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint(1)   NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_phone` (`phone` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户档案表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`
(
    `id`          bigint         NOT NULL AUTO_INCREMENT COMMENT '??ID',
    `order_id`    bigint         NOT NULL COMMENT '??ID',
    `product_id`  bigint         NOT NULL COMMENT '??ID',
    `width`       decimal(10, 2) NOT NULL COMMENT '??(mm)',
    `height`      decimal(10, 2) NOT NULL COMMENT '??(mm)',
    `area`        decimal(10, 4) NOT NULL COMMENT '??(??)',
    `quantity`    int            NOT NULL DEFAULT 1 COMMENT '??',
    `unit_price`  decimal(10, 2) NOT NULL COMMENT '????(?/??)',
    `total_price` decimal(10, 2) NOT NULL COMMENT '??',
    `color`       varchar(50)    NULL     DEFAULT NULL COMMENT '?????',
    `glass_spec`  varchar(100)   NULL     DEFAULT NULL COMMENT '???????',
    `remark`      varchar(255)   NULL     DEFAULT NULL COMMENT '??',
    `is_deleted`  tinyint(1)     NULL     DEFAULT 0 COMMENT '????',
    `create_time` datetime       NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '????',
    `update_time` datetime       NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '???????'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_payment
-- ----------------------------
DROP TABLE IF EXISTS `order_payment`;
CREATE TABLE `order_payment`
(
    `id`          bigint         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `order_id`    bigint         NOT NULL COMMENT '订单ID',
    `amount`      decimal(10, 2) NOT NULL COMMENT '收款金额',
    `pay_time`    datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收款时间',
    `pay_method`  varchar(50)    NULL     DEFAULT NULL COMMENT '支付方式（微信、支付宝、现金、转账）',
    `remark`      varchar(200)   NULL     DEFAULT NULL COMMENT '备注',
    `create_by`   bigint         NULL     DEFAULT NULL COMMENT '操作人ID',
    `create_time` datetime       NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_order_id` (`order_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单收款记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for order_payment_attachment
-- ----------------------------
DROP TABLE IF EXISTS `order_payment_attachment`;
CREATE TABLE `order_payment_attachment`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `payment_id`  bigint       NOT NULL COMMENT '支付记录ID',
    `url`         varchar(255) NOT NULL COMMENT '附件URL',
    `create_time` datetime     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_payment_id` (`payment_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 23
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单支付附件表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`
(
    `id`            bigint         NOT NULL AUTO_INCREMENT COMMENT '??ID',
    `category_id`   bigint         NOT NULL COMMENT '??ID',
    `brand_id`      bigint         NULL     DEFAULT NULL COMMENT '??ID',
    `name`          varchar(100)   NOT NULL COMMENT '?????????70???????',
    `code`          varchar(50)    NULL     DEFAULT NULL COMMENT '????',
    `base_price`    decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '??????/???',
    `color_options` varchar(200)   NULL     DEFAULT NULL COMMENT '??????????',
    `glass_options` varchar(200)   NULL     DEFAULT NULL COMMENT '??????????',
    `description`   text           NULL COMMENT '????',
    `status`        varchar(20)    NULL     DEFAULT 'ACTIVE' COMMENT '???ACTIVE, INACTIVE',
    `is_deleted`    tinyint(1)     NULL     DEFAULT 0 COMMENT '????',
    `create_by`     bigint         NULL     DEFAULT NULL COMMENT '???ID',
    `update_by`     bigint         NULL     DEFAULT NULL COMMENT '???ID',
    `create_time`   datetime       NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '????',
    `update_time`   datetime       NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '???'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '??ID',
    `name`        varchar(50) NOT NULL COMMENT '????????????????????',
    `sort`        int         NULL DEFAULT 0 COMMENT '??',
    `is_deleted`  tinyint(1)  NULL DEFAULT 0 COMMENT '????',
    `create_time` datetime    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '????',
    `update_time` datetime    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '????',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '?????'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for remeasure_task
-- ----------------------------
DROP TABLE IF EXISTS `remeasure_task`;
CREATE TABLE `remeasure_task`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `order_id`       bigint       NOT NULL COMMENT '订单ID',
    `assignee_id`    bigint       NOT NULL COMMENT '指派师傅ID',
    `status`         varchar(20)  NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING（待复尺）、COMPLETED（已完成）',
    `precise_width`  double       NULL     DEFAULT NULL COMMENT '精确宽度',
    `precise_height` double       NULL     DEFAULT NULL COMMENT '精确高度',
    `sketch_url`     varchar(255) NULL     DEFAULT NULL COMMENT '手绘图URL',
    `site_photos`    text         NULL COMMENT '现场图URL列表（JSON）',
    `remark`         varchar(500) NULL     DEFAULT NULL COMMENT '备注',
    `create_time`    datetime     NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime     NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_order_id` (`order_id` ASC) USING BTREE,
    INDEX `idx_assignee_id` (`assignee_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '复尺任务表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sales_target
-- ----------------------------
DROP TABLE IF EXISTS `sales_target`;
CREATE TABLE `sales_target`
(
    `id`             bigint         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `salesperson_id` bigint         NOT NULL COMMENT '销售员ID',
    `target_month`   varchar(7)     NOT NULL COMMENT '目标月份，格式：YYYY-MM',
    `target_amount`  decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '目标销售额',
    `create_by`      bigint         NULL     DEFAULT NULL COMMENT '创建人ID',
    `create_time`    datetime       NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      bigint         NULL     DEFAULT NULL COMMENT '更新人ID',
    `update_time`    datetime       NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_sales_month` (`salesperson_id` ASC, `target_month` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '销售目标表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_export_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_export_task`;
CREATE TABLE `sys_export_task`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_name`     varchar(100) NOT NULL COMMENT '任务名称',
    `status`        varchar(20)  NOT NULL COMMENT '状态: PENDING-待处理, PROCESSING-处理中, COMPLETED-完成, FAILED-失败',
    `file_url`      varchar(500) NULL DEFAULT NULL COMMENT '文件下载地址',
    `file_name`     varchar(255) NULL DEFAULT NULL COMMENT '文件名',
    `error_msg`     text         NULL COMMENT '错误信息',
    `create_by`     bigint       NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`   datetime     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `finish_time`   datetime     NULL DEFAULT NULL COMMENT '完成时间',
    `export_type`   varchar(50)  NULL DEFAULT NULL COMMENT '导出类型: ORDER-订单, etc.',
    `export_params` text         NULL COMMENT '导出参数(JSON)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_create_by` (`create_by` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 29
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '导出任务表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     bigint       NULL DEFAULT NULL COMMENT '操作人ID',
    `username`    varchar(50)  NULL DEFAULT NULL COMMENT '操作人用户名',
    `module`      varchar(50)  NULL DEFAULT NULL COMMENT '模块名称',
    `operation`   varchar(50)  NULL DEFAULT NULL COMMENT '操作类型',
    `method`      varchar(200) NULL DEFAULT NULL COMMENT '方法名称',
    `params`      text         NULL COMMENT '请求参数',
    `ip`          varchar(50)  NULL DEFAULT NULL COMMENT 'IP地址',
    `create_time` datetime     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `status`      tinyint(1)   NULL DEFAULT 1 COMMENT '状态(1:成功 0:失败)',
    `error_msg`   text         NULL COMMENT '错误信息',
    `cost_time`   bigint       NULL DEFAULT NULL COMMENT '耗时(毫秒)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 19052
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_code`   varchar(20) NOT NULL COMMENT '角色编码（英文）',
    `role_name`   varchar(50) NOT NULL COMMENT '角色名称（中文）',
    `create_time` datetime    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_role_code` (`role_code` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(50)  NOT NULL COMMENT '用户名',
    `password`    varchar(100) NOT NULL COMMENT '密码',
    `real_name`   varchar(50)  NULL DEFAULT NULL COMMENT '真实姓名',
    `role`        varchar(20)  NOT NULL COMMENT '角色：ADMIN（管理员）、SALES（销售员）、INSTALLER（安装师傅）',
    `create_by`   bigint       NULL DEFAULT NULL COMMENT '创建人ID',
    `update_by`   bigint       NULL DEFAULT NULL COMMENT '更新人ID',
    `is_deleted`  tinyint(1)   NULL DEFAULT 0 COMMENT '是否删除（0：否，1：是）',
    `create_time` datetime     NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_username` (`username` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for window_order
-- ----------------------------
DROP TABLE IF EXISTS `window_order`;
CREATE TABLE `window_order`
(
    `id`                      bigint         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `order_no`                varchar(50)    NOT NULL COMMENT '订单编号',
    `customer_name`           varchar(50)    NULL DEFAULT NULL COMMENT '客户姓名',
    `customer_phone`          varchar(20)    NULL DEFAULT NULL COMMENT '客户电话',
    `customer_id`             bigint         NULL DEFAULT NULL COMMENT '关联客户ID',
    `address`                 varchar(200)   NULL DEFAULT NULL COMMENT '安装地址（完整）',
    `province`                varchar(20)    NULL DEFAULT NULL COMMENT '省份编码',
    `city`                    varchar(20)    NULL DEFAULT NULL COMMENT '城市编码',
    `district`                varchar(20)    NULL DEFAULT NULL COMMENT '区县编码',
    `region_codes`            varchar(100)   NULL DEFAULT NULL COMMENT '区域编码（如：110000,110100）',
    `detail_address`          varchar(255)   NULL DEFAULT NULL COMMENT '用户输入的详细地址',
    `brand`                   varchar(50)    NULL DEFAULT NULL COMMENT '窗户品牌',
    `window_type`             varchar(50)    NULL DEFAULT NULL COMMENT '窗型（如：推拉、平开、内倒）',
    `color`                   varchar(50)    NULL DEFAULT NULL COMMENT '颜色',
    `glass_spec`              varchar(50)    NULL DEFAULT NULL COMMENT '玻璃规格',
    `width`                   double         NULL DEFAULT NULL COMMENT '宽度（毫米）',
    `height`                  double         NULL DEFAULT NULL COMMENT '高度（毫米）',
    `is_remeasured`           tinyint(1)     NULL DEFAULT 0 COMMENT '是否复尺（0：否，1：是）',
    `price`                   decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
    `paid_amount`             decimal(10, 2) NULL DEFAULT 0.00 COMMENT '已付金额',
    `payment_status`          varchar(20)    NULL DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID（未支付）、PARTIAL（部分支付）、PAID（已付清）',
    `order_time`              datetime       NULL DEFAULT NULL COMMENT '下单时间',
    `install_time`            datetime       NULL DEFAULT NULL COMMENT '计划安装时间',
    `install_progress`        varchar(20)    NULL DEFAULT 'WAITING' COMMENT '安装进度：WAITING（待安排）、SCHEDULED（已预约）、INSTALLING（安装中）、FINISHED（已完成）',
    `production_progress`     varchar(20)    NULL DEFAULT 'WAITING' COMMENT '制作进度：WAITING（待制作）、PRODUCING（制作中）、FINISHED（已完成）',
    `logistics_status`        varchar(20)    NULL DEFAULT NULL COMMENT '物流状态：OUTBOUND（已出库）、SHIPPING（送货中）、INBOUND（已入库）',
    `status`                  varchar(20)    NULL DEFAULT 'SUBMITTED' COMMENT '订单状态：DRAFT（草稿）、SUBMITTED（已提交）',
    `salesperson_id`          bigint         NULL DEFAULT NULL COMMENT '销售员ID',
    `installer_id`            bigint         NULL DEFAULT NULL COMMENT '安装师傅ID',
    `create_by`               bigint         NULL DEFAULT NULL COMMENT '创建人ID',
    `update_by`               bigint         NULL DEFAULT NULL COMMENT '更新人ID',
    `is_deleted`              tinyint(1)     NULL DEFAULT 0 COMMENT '是否删除（0：否，1：是）',
    `scheduled_install_date`  datetime       NULL DEFAULT NULL COMMENT '预约安装日期',
    `actual_install_end_date` datetime       NULL DEFAULT NULL COMMENT '实际安装完成日期',
    `create_time`             datetime       NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`             datetime       NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_order_no` (`order_no` ASC) USING BTREE,
    INDEX `idx_salesperson` (`salesperson_id` ASC) USING BTREE,
    INDEX `idx_installer` (`installer_id` ASC) USING BTREE,
    INDEX `idx_customer_id` (`customer_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '窗户订单表'
  ROW_FORMAT = DYNAMIC;

-- =========================================================
-- 1. 供应商表 (Supplier)
-- =========================================================
CREATE TABLE `supplier`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT,
    `name`            varchar(100) NOT NULL COMMENT '供应商名称',
    `contact_person`  varchar(50)    DEFAULT NULL COMMENT '联系人',
    `phone`           varchar(20)    DEFAULT NULL COMMENT '联系电话',
    `address`         varchar(255)   DEFAULT NULL COMMENT '地址',
    `account_balance` decimal(10, 2) DEFAULT '0.00' COMMENT '账款余额(应付账款)',
    `remark`          varchar(500)   DEFAULT NULL COMMENT '备注',
    `create_by`       bigint(20)     DEFAULT NULL,
    `create_time`     datetime       DEFAULT CURRENT_TIMESTAMP,
    `update_by`       bigint(20)     DEFAULT NULL,
    `update_time`     datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`      tinyint(1)     DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='供应商表';

-- =========================================================
-- 2. 材料分类表 (Material Category)
-- =========================================================
CREATE TABLE `material_category`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT,
    `name`        varchar(50) NOT NULL COMMENT '分类名称',
    `sort`        int(11)    DEFAULT '0' COMMENT '排序',
    `create_by`   bigint(20) DEFAULT NULL,
    `create_time` datetime   DEFAULT CURRENT_TIMESTAMP,
    `update_by`   bigint(20) DEFAULT NULL,
    `update_time` datetime   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`  tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='材料分类表';

-- =========================================================
-- 3. 材料表 (Material)
-- =========================================================
CREATE TABLE `material`
(
    `id`               bigint(20)   NOT NULL AUTO_INCREMENT,
    `category_id`      bigint(20)   NOT NULL COMMENT '分类ID',
    `name`             varchar(100) NOT NULL COMMENT '材料名称',
    `code`             varchar(50)    DEFAULT NULL COMMENT '材料编码',
    `spec`             varchar(100)   DEFAULT NULL COMMENT '规格型号',
    `unit`             varchar(20)    DEFAULT NULL COMMENT '单位(如: 米, 平方米, 个, 支)',
    `unit_price`       decimal(10, 2) DEFAULT '0.00' COMMENT '参考单价',
    `stock_quantity`   decimal(10, 2) DEFAULT '0.00' COMMENT '当前库存数量',
    `warning_quantity` decimal(10, 2) DEFAULT '0.00' COMMENT '库存预警阈值',
    `remark`           varchar(500)   DEFAULT NULL COMMENT '备注',
    `create_by`        bigint(20)     DEFAULT NULL,
    `create_time`      datetime       DEFAULT CURRENT_TIMESTAMP,
    `update_by`        bigint(20)     DEFAULT NULL,
    `update_time`      datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`       tinyint(1)     DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='材料表';

-- =========================================================
-- 4. 采购订单表 (Purchase Order)
-- =========================================================
CREATE TABLE `purchase_order`
(
    `id`            bigint(20)  NOT NULL AUTO_INCREMENT,
    `order_no`      varchar(50) NOT NULL COMMENT '采购单号',
    `supplier_id`   bigint(20)  NOT NULL COMMENT '供应商ID',
    `total_amount`  decimal(10, 2) DEFAULT '0.00' COMMENT '订单总金额',
    `paid_amount`   decimal(10, 2) DEFAULT '0.00' COMMENT '已付金额',
    `status`        varchar(20)    DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿, PENDING-待入库, COMPLETED-已完成, CANCELLED-已取消',
    `purchase_date` date           DEFAULT NULL COMMENT '采购日期',
    `remark`        varchar(500)   DEFAULT NULL COMMENT '备注',
    `create_by`     bigint(20)     DEFAULT NULL,
    `create_time`   datetime       DEFAULT CURRENT_TIMESTAMP,
    `update_by`     bigint(20)     DEFAULT NULL,
    `update_time`   datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted`    tinyint(1)     DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='采购订单表';

-- =========================================================
-- 5. 采购订单明细表 (Purchase Order Item)
-- =========================================================
CREATE TABLE `purchase_order_item`
(
    `id`                bigint(20)     NOT NULL AUTO_INCREMENT,
    `purchase_order_id` bigint(20)     NOT NULL COMMENT '采购订单ID',
    `material_id`       bigint(20)     NOT NULL COMMENT '材料ID',
    `quantity`          decimal(10, 2) NOT NULL COMMENT '采购数量',
    `unit_price`        decimal(10, 2) NOT NULL COMMENT '采购单价',
    `total_price`       decimal(10, 2) NOT NULL COMMENT '小计金额',
    `remark`            varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_purchase_order` (`purchase_order_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='采购订单明细表';

-- =========================================================
-- 6. 库存记录表/出入库流水 (Inventory Record)
-- =========================================================
CREATE TABLE `inventory_record`
(
    `id`              bigint(20)     NOT NULL AUTO_INCREMENT,
    `type`            varchar(20)    NOT NULL COMMENT '类型: INBOUND-入库, OUTBOUND-出库',
    `material_id`     bigint(20)     NOT NULL COMMENT '材料ID',
    `quantity`        decimal(10, 2) NOT NULL COMMENT '变动数量',
    `before_quantity` decimal(10, 2) NOT NULL COMMENT '变动前库存',
    `after_quantity`  decimal(10, 2) NOT NULL COMMENT '变动后库存',
    `relation_type`   varchar(50)  DEFAULT NULL COMMENT '关联单据类型: PURCHASE-采购入库, ORDER-订单出库, MANUAL-手动调整',
    `relation_id`     bigint(20)   DEFAULT NULL COMMENT '关联单据ID',
    `operator_id`     bigint(20)   DEFAULT NULL COMMENT '操作人ID',
    `remark`          varchar(255) DEFAULT NULL COMMENT '备注',
    `create_time`     datetime     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_material` (`material_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='库存变动记录表';

-- =========================================================
-- 7. 订单成本核算表 (Order Cost)
-- =========================================================
CREATE TABLE `order_cost`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT,
    `order_id`          bigint(20) NOT NULL COMMENT '关联客户订单ID',
    `material_cost`     decimal(10, 2) DEFAULT '0.00' COMMENT '材料总成本',
    `labor_cost`        decimal(10, 2) DEFAULT '0.00' COMMENT '人工成本',
    `other_cost`        decimal(10, 2) DEFAULT '0.00' COMMENT '其他成本(如运费、餐补)',
    `total_cost`        decimal(10, 2) DEFAULT '0.00' COMMENT '总成本',
    `order_amount`      decimal(10, 2) DEFAULT '0.00' COMMENT '订单总金额(收入)',
    `gross_profit`      decimal(10, 2) DEFAULT '0.00' COMMENT '毛利润',
    `gross_profit_rate` decimal(5, 2)  DEFAULT '0.00' COMMENT '毛利率(%)',
    `remark`            varchar(500)   DEFAULT NULL COMMENT '成本核算备注',
    `create_by`         bigint(20)     DEFAULT NULL,
    `create_time`       datetime       DEFAULT CURRENT_TIMESTAMP,
    `update_by`         bigint(20)     DEFAULT NULL,
    `update_time`       datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order` (`order_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单成本核算表';


SET FOREIGN_KEY_CHECKS = 1;
