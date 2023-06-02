DROP TABLE IF EXISTS `chat_msgs`;
CREATE TABLE `chat_msgs` (
   `id` bigint(20) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主表id',
   `user_id` VARCHAR(64) DEFAULT NULL COMMENT '用户id',
   `model` tinyint(1) DEFAULT NULL COMMENT '使用的模型',
   `chat_id` VARCHAR(64) NOT NULL COMMENT '一个对话id，包含多个消息，用户提问和ai回答',
   `msg_id` VARCHAR(64) NOT NULL COMMENT '一个对话下的消息id',
   `n_msg_id` VARCHAR(64) DEFAULT NULL COMMENT '当前消息的下一个消息id',
   `content` TEXT DEFAULT NULL COMMENT '消息信息，请求的text 或者 ai返回的文本信息',
   `role` tinyint(1) NOT NULL COMMENT '消息对应角色',
   `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `timestamp` BIGINT NOT NULL COMMENT '请求时间',
   `ptd` INT NOT NULL COMMENT '请求日期yyyyMMdd',
   UNIQUE KEY `chat_msg` (`chat_id`,`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT "请求消息表";

