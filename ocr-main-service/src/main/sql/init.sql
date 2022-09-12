DROP TABLE IF EXISTS `ocr_request_log`;
CREATE TABLE `ocr_request_log` (
   `id` bigint(20) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主表id',
   `ip` VARCHAR(64) DEFAULT NULL COMMENT '请求客户端ip',
   `user_id` VARCHAR(64) DEFAULT NULL COMMENT '用户id',
   `request_id` VARCHAR(64) DEFAULT NULL COMMENT '请求id',
   `ua` VARCHAR(1024) DEFAULT NULL COMMENT '请求ua',
   `refer` VARCHAR(1024) DEFAULT NULL COMMENT '请求refer',
   `timestamp` BIGINT NOT NULL COMMENT '请求实践',
   `sdev_id` VARCHAR(128) DEFAULT NULL COMMENT '请求客户端设备id',
   `device_data` VARCHAR(1024) DEFAULT NULL COMMENT '请求客户端设备数据',
   `ptd` INT NOT NULL COMMENT '请求日期yyyyMMdd'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "请求日志表";