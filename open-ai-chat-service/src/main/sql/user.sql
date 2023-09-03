use ocr;

--DROP TABLE IF EXISTS `user_base`;
CREATE TABLE `user_base` (
    `id` bigint(20) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主表id',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `mobile` VARCHAR(128) DEFAULT NULL COMMENT '手机',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `nick_name` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    `username` VARCHAR(64) DEFAULT NULL COMMENT '用户名',
    `password` VARCHAR(128) DEFAULT NULL COMMENT '用户密码',
    `gender` VARCHAR(2) DEFAULT NULL COMMENT '用户性别',
    `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ptd` INT NOT NULL COMMENT '请求日期yyyyMMdd',
    UNIQUE KEY `user_id_unique_key` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "用户基础信息表";

-- DROP TABLE IF EXISTS `user_third_party`;
CREATE TABLE `user_third_party` (
    `id` bigint(20) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主表id',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
    `union_id` VARCHAR(128) DEFAULT NULL COMMENT '开发者主体对应的用户id',
    `open_id` VARCHAR(128) DEFAULT NULL COMMENT '开发者应用对应的id',
    `privilege` VARCHAR(1024) DEFAULT NULL COMMENT '权限',
    `platform` INT DEFAULT NULL COMMENT '账号平台',
    `app_id` VARCHAR(128) DEFAULT NULL COMMENT '平台应用id',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ptd` INT NOT NULL COMMENT '请求日期yyyyMMdd'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "用户三方平台账号信息表";

