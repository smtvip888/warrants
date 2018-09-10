SET FOREIGN_KEY_CHECKS = 0;


alter table Logs modify Remark mediumtext COMMENT '备注';
alter table sys_config modify value mediumtext COMMENT '参数值';

INSERT INTO `resource`( `pid`, `uri`, `icon`, `name`, `description`, `create_time`) VALUES (28, '/admin/notice/list', '', '通知公告', '', '2018-02-12 14:19:57');

INSERT INTO `sys_config`(`key`, `value`, `remark`) VALUES ('notice', '{\"adminUserId\":15,\"body\":\"<p>新春快乐。</p><p>Happy New Year!</p>\",\"lang\":\"cn\",\"show\":true,\"summary\":\"测试\",\"title\":\"公告  Notice\"}', '登录公告');


SET FOREIGN_KEY_CHECKS = 1;
