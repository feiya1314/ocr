DROP TABLE IF EXISTS `ocr_request_log`;
CREATE TABLE `ocr_request_log` (
   `id` bigint(20) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主表id',
   `ip` VARCHAR(64) DEFAULT NULL COMMENT '请求客户端ip',
   `user_id` VARCHAR(64) DEFAULT NULL COMMENT '用户id',
   `request_id` VARCHAR(64) DEFAULT NULL COMMENT '请求id',
   `origin` VARCHAR(64) DEFAULT NULL COMMENT '请求来源接口',
   `ua` VARCHAR(1024) DEFAULT NULL COMMENT '请求ua',
   `refer` VARCHAR(1024) DEFAULT NULL COMMENT '请求refer',
   `timestamp` BIGINT NOT NULL COMMENT '请求实践',
   `sdev_id` VARCHAR(128) DEFAULT NULL COMMENT '请求客户端设备id',
   `device_data` VARCHAR(1024) DEFAULT NULL COMMENT '请求客户端设备数据',
   `ptd` INT NOT NULL COMMENT '请求日期yyyyMMdd'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "请求日志表";

DROP TABLE IF EXISTS `ocr_source_info`;
CREATE TABLE `ocr_source_info` (
   `id` int unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主表id',
   `source_id` VARCHAR(64) NOT NULL COMMENT 'ocr 源唯一id',
   `source_name` VARCHAR(64) NOT NULL COMMENT 'ocr 源名称',
   `type` INT NOT NULL DEFAULT  0 COMMENT 'ocr类型，比如通用类型，身份证识别等',
   `enable` INT NOT NULL DEFAULT 0 COMMENT '是否开启1开启0不开启',
   `grey` INT NOT NULL DEFAULT 0 COMMENT '是否灰度1开启0不开启',
   `day_limit` INT NOT NULL DEFAULT 0 COMMENT '每日限额',
   `day_used` INT NOT NULL DEFAULT 0 COMMENT '已使用',
   `month_limit` INT NOT NULL DEFAULT 0 COMMENT '每月限额',
   `month_used` INT NOT NULL DEFAULT 0 COMMENT '已使用',
   `year_limit` INT NOT NULL DEFAULT 0 COMMENT '每年限额',
   `year_used` INT NOT NULL DEFAULT 0 COMMENT '已使用',
   `params_config` VARCHAR(1024) DEFAULT NULL COMMENT '额外配置',
   `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
   `update_time` TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "请求日志表";