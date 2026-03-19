-- =========================================================
-- 1. 供应商表 (Supplier)
-- =========================================================
CREATE TABLE `supplier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '供应商名称',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `account_balance` decimal(10,2) DEFAULT '0.00' COMMENT '账款余额(应付账款)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- =========================================================
-- 2. 材料分类表 (Material Category)
-- =========================================================
CREATE TABLE `material_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='材料分类表';

-- =========================================================
-- 3. 材料表 (Material)
-- =========================================================
CREATE TABLE `material` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `name` varchar(100) NOT NULL COMMENT '材料名称',
  `code` varchar(50) DEFAULT NULL COMMENT '材料编码',
  `spec` varchar(100) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位(如: 米, 平方米, 个, 支)',
  `unit_price` decimal(10,2) DEFAULT '0.00' COMMENT '参考单价',
  `stock_quantity` decimal(10,2) DEFAULT '0.00' COMMENT '当前库存数量',
  `warning_quantity` decimal(10,2) DEFAULT '0.00' COMMENT '库存预警阈值',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='材料表';

-- =========================================================
-- 4. 采购订单表 (Purchase Order)
-- =========================================================
CREATE TABLE `purchase_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL COMMENT '采购单号',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '订单总金额',
  `paid_amount` decimal(10,2) DEFAULT '0.00' COMMENT '已付金额',
  `status` varchar(20) DEFAULT 'DRAFT' COMMENT '状态: DRAFT-草稿, PENDING-待入库, COMPLETED-已完成, CANCELLED-已取消',
  `purchase_date` date DEFAULT NULL COMMENT '采购日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

-- =========================================================
-- 5. 采购订单明细表 (Purchase Order Item)
-- =========================================================
CREATE TABLE `purchase_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_order_id` bigint(20) NOT NULL COMMENT '采购订单ID',
  `material_id` bigint(20) NOT NULL COMMENT '材料ID',
  `quantity` decimal(10,2) NOT NULL COMMENT '采购数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '采购单价',
  `total_price` decimal(10,2) NOT NULL COMMENT '小计金额',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_purchase_order` (`purchase_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单明细表';

-- =========================================================
-- 6. 库存记录表/出入库流水 (Inventory Record)
-- =========================================================
CREATE TABLE `inventory_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL COMMENT '类型: INBOUND-入库, OUTBOUND-出库',
  `material_id` bigint(20) NOT NULL COMMENT '材料ID',
  `quantity` decimal(10,2) NOT NULL COMMENT '变动数量',
  `before_quantity` decimal(10,2) NOT NULL COMMENT '变动前库存',
  `after_quantity` decimal(10,2) NOT NULL COMMENT '变动后库存',
  `relation_type` varchar(50) DEFAULT NULL COMMENT '关联单据类型: PURCHASE-采购入库, ORDER-订单出库, MANUAL-手动调整',
  `relation_id` bigint(20) DEFAULT NULL COMMENT '关联单据ID',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_material` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存变动记录表';

-- =========================================================
-- 7. 订单成本核算表 (Order Cost)
-- =========================================================
CREATE TABLE `order_cost` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '关联客户订单ID',
  `material_cost` decimal(10,2) DEFAULT '0.00' COMMENT '材料总成本',
  `labor_cost` decimal(10,2) DEFAULT '0.00' COMMENT '人工成本',
  `other_cost` decimal(10,2) DEFAULT '0.00' COMMENT '其他成本(如运费、餐补)',
  `total_cost` decimal(10,2) DEFAULT '0.00' COMMENT '总成本',
  `order_amount` decimal(10,2) DEFAULT '0.00' COMMENT '订单总金额(收入)',
  `gross_profit` decimal(10,2) DEFAULT '0.00' COMMENT '毛利润',
  `gross_profit_rate` decimal(5,2) DEFAULT '0.00' COMMENT '毛利率(%)',
  `remark` varchar(500) DEFAULT NULL COMMENT '成本核算备注',
  `create_by` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单成本核算表';

-- 插入一些测试分类数据
INSERT INTO `material_category` (`id`, `name`, `sort`) VALUES
(1, '铝型材', 1),
(2, '玻璃', 2),
(3, '五金配件', 3),
(4, '密封胶条', 4),
(5, '包装耗材', 5);
