

INSERT INTO `sys_role`
VALUES (1, 'ADMIN', '管理员', '2026-01-26 10:17:00');
INSERT INTO `sys_role`
VALUES (2, 'SALES', '销售员', '2026-01-26 10:17:00');
INSERT INTO `sys_role`
VALUES (3, 'INSTALLER', '安装师傅', '2026-01-26 10:21:34');

INSERT INTO `sys_user`
VALUES (1, 'admin', '$2a$10$4xSdvxnIbWscqBkV4Dmz6eMwsk7uTbZum4.4llJ2lnRY8hX8i58ZO', '老板',
        'ADMIN', NULL, 1, 0, '2026-01-23 18:24:30');
INSERT INTO `sys_user`
VALUES (2, 'sales_chen', '$2a$10$0CGz5TjSDPUdK7ov3yZkk.cZol89Kf8vvYybmlDQ6C7uUCBXRCzxO', '陈女士',
        'SALES', 1, 1, 0, '2026-01-23 18:24:30');
INSERT INTO `sys_user`
VALUES (3, 'sales_wang', '$2a$10$dCgqRa2Ap.DKJ3ldt8pJ5OJXdZW8UkRjVrGZfz7IWJA5NQ7FtTxK.', '王女士',
        'SALES', 1, 1, 0, '2026-01-26 10:06:49');
INSERT INTO `sys_user`
VALUES (4, 'install_chen', '$2a$10$hc7//F4gvDtvqQohp2Xo6.XGyvoQ6oXISlu1HhXAb0G2E7fkX.1Za',
        '陈师傅', 'INSTALLER', 1, 1, 0, '2026-01-26 10:21:34');
INSERT INTO `sys_user`
VALUES (5, 'install_sun', '$2a$10$GGIjc05X.6jEULXE.OQ.aeCY4Z9alS95JzMcSrq60ylrcnExnQY2.', '孙师傅',
        'INSTALLER', 1, 1, 0, '2026-01-26 10:42:47');
INSERT INTO `sys_user`
VALUES (6, 'sunjian2', '$2a$10$MHhdxmcEH07OBh07TX4.zudOzSc9tYjQrMYWNkdmFnpzJDdC1q.YW', '孙健',
        'ADMIN', 1, NULL, 0, '2026-01-27 09:40:35');


INSERT INTO `brand` (`name`, `description`, `is_deleted`)
VALUES ('皇派门窗', '国内知名高端隔音门窗品牌，主打系统门窗、阳光房', 0),
       ('派雅门窗', '专注于高端铝合金门窗、系统门窗的定制', 0),
       ('轩尼斯门窗', '知名铝合金门窗品牌，产品涵盖断桥铝门窗、阳光房等', 0),
       ('新豪轩门窗', '集门窗产品研发、设计、生产、销售于一体的综合性企业', 0),
       ('飞宇门窗', '主打高端定制铝合金门窗、阳光房系列产品', 0),
       ('欧哲门窗', '高端系统门窗品牌，主打安全、节能、环保门窗', 0),
       ('墨瑟门窗', '源自德国的高端系统门窗品牌，以木铝复合门窗见长', 0),
       ('森鹰门窗', '以铝包木空调窗为主导产品的高端门窗企业', 0),
       ('良木道门窗', '专注于实木、铝包木等高端木窗的定制', 0),
       ('维盾门窗', '集研发、生产、销售、服务为一体的高端系统门窗品牌', 0),
       ('罗兰西尼', '专注于高端系统门窗、阳光房的定制服务', 0),
       ('亿合门窗', '产品线涵盖铝合金门窗、阳光房、原木门等', 0),
       ('百利玛门窗', '法式轻奢门窗品牌，主推高端系统门窗', 0),
       ('富轩门窗', '全屋门窗定制品牌，涵盖铝合金门窗、智能门窗等', 0),
       ('尊尚门窗', '主打高端铝合金门窗、阳光房等定制产品', 0);


-- =========================================================
-- 1. 插入产品分类 (product_category)
-- =========================================================
INSERT INTO `product_category` (`id`, `name`, `sort`, `is_deleted`)
VALUES (1, '平开窗/内开内倒', 1, 0),
       (2, '推拉窗/推拉门', 2, 0),
       (3, '阳光房系列', 3, 0),
       (4, '入户门/极简门', 4, 0);

-- =========================================================
-- 2. 插入产品库数据 (product)
-- 涵盖目前数据库中的 15 个品牌 (id: 1-15)
-- =========================================================

-- 品牌1: 皇派门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 1, '皇派109管家系列外开窗', 'HP-109-WK', 1280.00, '氟碳灰,金属咖啡,珍珠白',
        '5+27A+5双层中空,5+20A+5Low-E', '标配进口五金，隔音降噪性能优异', 'ACTIVE'),
       (2, 1, '皇派115重型推拉门', 'HP-115-TL', 1580.00, '氟碳灰,红酸枝,金属咖啡',
        '5+12A+5双层中空', '下轨带挡水边，抗风压性能极佳', 'ACTIVE'),
       (3, 1, '皇派维多利亚阳光房', 'HP-YGF-WD', 3200.00, '氟碳灰,砂纹黑', '6+12A+6夹胶钢化',
        '高强度铝合金骨架，断桥隔热设计', 'ACTIVE');

-- 品牌2: 派雅门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 2, '派雅领普75系列内开内倒', 'PY-75-NK', 1150.00, '氟碳灰,肌肤黑,太空灰',
        '5+20A+5双层中空', '多腔体结构，保温隔热效果好', 'ACTIVE'),
       (4, 2, '派雅极简窄边平开门', 'PY-ZJ-PK', 980.00, '极简黑,氟碳白', '8mm单层钢化,长虹玻璃',
        '卫生间/厨房专用，极窄边框设计', 'ACTIVE');

-- 品牌3: 轩尼斯门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 3, '轩尼斯圣世系列85平开窗', 'XNS-85-PK', 1350.00, '砂纹灰,金属银',
        '5+12A+5+12A+5三层中空', '高配三层中空玻璃，极致隔音', 'ACTIVE'),
       (2, 3, '轩尼斯120断桥推拉窗', 'XNS-120-TL', 1420.00, '砂纹灰,红木纹', '5+12A+5双层中空',
        '高低轨设计，排水更顺畅', 'ACTIVE');

-- 品牌4: 新豪轩门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 4, '新豪轩埃尔斯110系统窗', 'XHX-110-XT', 1450.00, '太空灰,氟碳金', '5+27A+5中空钢化',
        '隐藏式排水设计，德国好博五金', 'ACTIVE'),
       (3, 4, '新豪轩星空顶阳光房', 'XHX-YGF-XK', 3500.00, '太空灰,砂纹黑', '8+1.14PVB+8夹胶',
        '全铝合金结构，承重力强', 'ACTIVE');

-- 品牌5: 飞宇门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 5, '飞宇世家90系列外开窗', 'FY-90-WK', 1180.00, '肌肤灰,胡桃木', '5+20A+5双层中空',
        '防蚊高透网一体化设计', 'ACTIVE'),
       (2, 5, '飞宇135极简重型推拉门', 'FY-135-TL', 1680.00, '肌肤黑,金属灰', '5+15A+5双层中空',
        '超大地弹簧滑轮，推拉静音', 'ACTIVE');

-- 品牌6: 欧哲门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 6, '欧哲自由境界100系统窗', 'OZ-100-XT', 2180.00, '高亮银,氟碳灰', '6+20A+6Low-E',
        '高端被动房标准，极致节能', 'ACTIVE'),
       (4, 6, '欧哲极隐系列装甲门', 'OZ-JY-ZJ', 4500.00, '星空灰,曜石黑', '无需玻璃',
        '智能密码锁，C级防盗锁芯', 'ACTIVE');

-- 品牌7: 墨瑟门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 7, '墨瑟Moser铝包木78系列', 'MS-78-LM', 2880.00, '橡木色,松木色,室外灰',
        '5+12A+5+12A+5三层中空', '外铝内木，德式工艺，尊贵典雅', 'ACTIVE'),
       (1, 7, '墨瑟Moser系统纯铝90系列', 'MS-90-CL', 1850.00, '室外灰,室内白', '5+20A+5Low-E',
        '纯铝系统窗，密封性能极佳', 'ACTIVE');

-- 品牌8: 森鹰门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 8, '森鹰S101铝包木空调窗', 'SY-S101', 3200.00, '红松本色,外铝深灰',
        '5+15A+5+15A+5三玻两腔', '被动式超低能耗建筑认证产品', 'ACTIVE'),
       (3, 8, '森鹰铝包木阳光房', 'SY-YGF', 4800.00, '红松本色,外铝深灰', '6+1.52PVB+6夹胶',
        '自然木韵，冬季保温效果好', 'ACTIVE');

-- 品牌9: 良木道门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 9, '良木道无缝焊接105系列', 'LMD-105-WF', 1580.00, '星际灰,宝马金', '5+24A+5中空钢化',
        '窗框无缝焊接工艺，不渗水', 'ACTIVE'),
       (2, 9, '良木道极简窄边推拉门', 'LMD-ZJ-TL', 1280.00, '极简黑', '8mm钢化玻璃,长虹玻璃',
        '网红极窄边框，视野开阔', 'ACTIVE');

-- 品牌10: 维盾门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 10, '维盾VD70系列断桥铝窗', 'VD-70-DQ', 980.00, '白色,灰色,香槟色', '5+27A+5双层中空',
        '高性价比断桥铝，适合大众家装', 'ACTIVE'),
       (1, 10, '维盾VD80系列窗纱一体', 'VD-80-CS', 1150.00, '灰色,咖啡色', '5+20A+5双层中空',
        '金刚网窗纱一体，防盗防蚊', 'ACTIVE');

-- 品牌11: 罗兰西尼
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 11, '罗兰西尼115系统隔音窗', 'LL-115-XT', 1680.00, '氟碳灰,砂纹黑', '5+12A+5+12A+5三层',
        '阶梯式排水，汽车级密封胶条', 'ACTIVE'),
       (3, 11, '罗兰西尼斜顶阳光房', 'LL-YGF-XD', 3100.00, '氟碳灰,砂纹黑', '6+1.14PVB+6夹胶',
        '专业防水设计，抗积雪积水', 'ACTIVE');

-- 品牌12: 亿合门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 12, '亿合天御100平开窗', 'YH-100-TY', 1380.00, '太空灰,黄花梨', '5+20A+5双层中空',
        '销钉注胶工艺，结构更稳固', 'ACTIVE'),
       (4, 12, '亿合PT折叠门', 'YH-PT-ZD', 1450.00, '太空灰,氟碳白', '5+9A+5双层中空',
        '推拉折叠两用，极致节省空间', 'ACTIVE');

-- 品牌13: 百利玛门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 13, '百利玛法式轻奢85系列', 'BLM-85-QS', 1550.00, '香槟金,氟碳灰', '5+12A+5Low-E',
        '圆弧压线设计，法式浪漫风格', 'ACTIVE'),
       (2, 13, '百利玛130全景推拉门', 'BLM-130-QJ', 1780.00, '极简黑,香槟金', '6+12A+6双层中空',
        '大玻璃占比，通透采光', 'ACTIVE');

-- 品牌14: 富轩门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 14, '富轩富美90窗纱一体', 'FX-90-FM', 1250.00, '砂纹灰,水曲柳', '5+20A+5双层中空',
        '标配进口五金，安全防撬锁点', 'ACTIVE'),
       (4, 14, '富轩铝合金卫浴门', 'FX-WYM', 850.00, '拉丝金,氟碳白', '5mm磨砂玻璃,油砂玻璃',
        '防水防潮，静音磁吸锁', 'ACTIVE');

-- 品牌15: 尊尚门窗
INSERT INTO `product` (`category_id`, `brand_id`, `name`, `code`, `base_price`, `color_options`,
                       `glass_options`, `description`, `status`)
VALUES (1, 15, '尊尚领航者108系列', 'ZS-108-LH', 1180.00, '氟碳灰,红酸枝', '5+27A+5双层中空',
        '多重密封设计，超强水密性', 'ACTIVE'),
       (2, 15, '尊尚极简联动推拉门', 'ZS-JJ-LD', 1350.00, '极简黑,金属灰', '8mm钢化玻璃',
        '三轨联动，一推到底，顺滑无比', 'ACTIVE');
