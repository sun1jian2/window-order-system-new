-- Add new columns to sys_operation_log table
ALTER TABLE sys_operation_log ADD COLUMN status TINYINT(1) DEFAULT 1 COMMENT '状态(1:成功 0:失败)';
ALTER TABLE sys_operation_log ADD COLUMN error_msg TEXT COMMENT '错误信息';
ALTER TABLE sys_operation_log ADD COLUMN cost_time BIGINT COMMENT '耗时(毫秒)';
