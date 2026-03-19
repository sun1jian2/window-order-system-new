CREATE TABLE `product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称（如：断桥铝窗、阳光房、系统门）',
  `sort` int DEFAULT 0 COMMENT '排序',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品分类表';

CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `name` varchar(100) NOT NULL COMMENT '产品名称（如：凤铝70系列内开内倒）',
  `code` varchar(50) DEFAULT NULL COMMENT '产品编码',
  `base_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '基础单价（元/平米）',
  `color_options` varchar(200) DEFAULT NULL COMMENT '可选颜色（逗号分隔）',
  `glass_options` varchar(200) DEFAULT NULL COMMENT '可选玻璃（逗号分隔）',
  `description` text COMMENT '产品描述',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE, INACTIVE',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `width` decimal(10,2) NOT NULL COMMENT '宽度(mm)',
  `height` decimal(10,2) NOT NULL COMMENT '高度(mm)',
  `area` decimal(10,4) NOT NULL COMMENT '面积(平米)',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '实际单价(元/平米)',
  `total_price` decimal(10,2) NOT NULL COMMENT '总价',
  `color` varchar(50) DEFAULT NULL COMMENT '选择的颜色',
  `glass_spec` varchar(100) DEFAULT NULL COMMENT '选择的玻璃规格',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单产品明细表';
