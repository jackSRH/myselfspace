-- ----------------------------
-- 1、操作记录表
-- ----------------------------
drop table if exists t_sys_oper_log;
create table t_sys_oper_log (
  id 				int(11) 		not null auto_increment    comment '日志主键',
  title             varchar(64)     default null               comment '模块标题',
  action            varchar(128)    default null               comment '功能请求',
  method            varchar(128)    default null               comment '方法名称',
  channel           int(4)		    default null               comment '来源渠道（1:web,2:app）',
  oper_name 	    varchar(32)     default null 		 	   comment '操作人员',
  oper_url 		    varchar(256) 	default null 			   comment '请求URL',
  oper_ip 			varchar(32) 	default null 			   comment '主机地址',
  oper_location     varchar(256)    default null               comment '操作地点',
  oper_param 		varchar(512) 	default null 			   comment '请求参数',
  status 			int(1) 			default null			   comment '操作状态（0正常 1异常）',
  error_msg 		varchar(2000) 	default null 			   comment '错误消息',
  create_by        	varchar(32)     DEFAULT null               COMMENT '创建者',
  create_time      	datetime                                   COMMENT '创建时间',
  primary key (id)
) engine=innodb auto_increment=1 default charset=utf8 comment = '操作日志记录';



-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists t_user;
CREATE TABLE `t_user` (
  `id` 				int(11) 		NOT NULL AUTO_INCREMENT,
  `user_name` 		varchar(32) 	DEFAULT NULL 				COMMENT '账号',
  `password` 		varchar(32) 	DEFAULT NULL 				COMMENT '密码',
  `full_name` 		varchar(32) 	DEFAULT NULL 				COMMENT '名称',
  `phone` 			varchar(16) 	DEFAULT NULL 				COMMENT '电话',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB auto_increment=1 default charset=utf8 comment = '用户信息';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into t_user values(1,'admin','21232f297a57a5a743894a0e4a801fc3','超级管理员','15999562568',0,now(),now(),'system','system');


-- ----------------------------
-- 3、角色信息表
-- ----------------------------
drop table if exists `t_sys_role`;
create table `t_sys_role` (
  `id` 				int(11) 		not null auto_increment    	comment '角色ID',
  `role_name` 		varchar(32) 	DEFAULT NULL 				comment '角色名称',
  `role_key` 		varchar(128) 	DEFAULT NULL 				comment '角色权限字符串',
  `role_sort`     	int(4)        	DEFAULT NULL               	comment '显示顺序',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  `remark` 			varchar(500) 	default NULL 				comment '备注',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '角色信息';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into `t_sys_role` values(1, '管理员', 'admin', 1, 0, NOW(), NOW(),'admin', 'admin', '管理员');
insert into `t_sys_role` values(2, '普通用户', 'common',  2,  0, NOW(), NOW(),'admin', 'admin', '普通用户');



-- ----------------------------
-- 4、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists `t_sys_user_role`;
create table `t_sys_user_role` (
  `id` 			int(11) 		NOT NULL	 AUTO_INCREMENT 	COMMENT '主键id',
  `user_id` 	int(11) 		DEFAULT NULL 					comment '用户ID',
  `role_id` 	int(11) 		DEFAULT NULL 					comment '角色ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_role_ids` (`user_id`,`role_id`) USING BTREE
) engine=innodb auto_increment=1 default charset=utf8 comment = '用户和角色关联表';


-- ----------------------------
-- 初始化-用户角色表数据
-- ----------------------------
insert into `t_sys_user_role` values (1,1, 1);