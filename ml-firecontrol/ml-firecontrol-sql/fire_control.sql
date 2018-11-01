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



-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists `t_sys_menu`;
create table `t_sys_menu` (
  `id` 					int(11) 			not null 	auto_increment    		comment '菜单ID',
  `menu_name` 			varchar(64) 		not null 				   			comment '菜单名称',
  `parent_id` 			int(11) 			default 0 			       			comment '父菜单ID',
  `order_num` 			int(4) 				default null 			   			comment '显示顺序',
  `url` 				varchar(256) 		default NULL				   		comment '请求地址',
  `route_path` 			varchar(256) 		default NULL				   		comment '路由地址',
  `menu_type` 			TINYINT(2) 			default 1 			       			comment '菜单类型（0目录 1菜单 2按钮  3链接）',
  `visible` 			TINYINT(1) 			default 0 				   			comment '菜单状态（0显示 1隐藏）',
  `perms` 				varchar(128) 		default NULL			   			comment '权限标识',
  `icon` 				varchar(128) 		default NULL			   			comment '菜单图标',
  `create_by`     		varchar(64)   		default NULL               			comment '创建者',
  `create_time` 		datetime                                  				comment '创建时间',
  `update_by` 			varchar(64) 		default NULL		       			comment '更新者',
  `update_time` 		datetime                                  				comment '更新时间',
  `remark` 				varchar(500) 		default NULL			   			comment '备注',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '菜单权限表';


-- ----------------------------
-- 初始化-菜单权限表数据
-- ----------------------------
insert into `t_sys_menu` values
(1, '首页', 0, 1, '/main/home','/main/home', 0, 0, 'page:home', '',   'admin', now(), 'admin', now(), '首页目录');



-- ----------------------------
-- 6、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists `t_sys_role_menu`;
create table `t_sys_role_menu` (
  `id` 					bigint(19) 			NOT NULL AUTO_INCREMENT 			COMMENT '主键id',
  `role_id` 			int(11) 			not null 							comment '角色ID',
  `menu_id` 			int(11) 			not null 							comment '菜单ID',
	PRIMARY KEY (`id`),
  KEY `idx_role_menu_ids` (`role_id`,`menu_id`) USING BTREE
) engine=innodb auto_increment=1 default charset=utf8 comment = '角色和菜单关联表';


-- ----------------------------
-- 初始化-用户角色表数据
-- ----------------------------
insert into `t_sys_role_menu`(`role_id`,`menu_id`) (select 1,m.menu_id from `t_sys_menu` m);



-- ----------------------------
-- 7、区域信息表
-- ----------------------------
drop table if exists `t_sys_area`;
create table `t_sys_area` (
  `id` 				int(11) 		not null auto_increment    	comment 'ID',
  `area_name` 		varchar(32) 	DEFAULT NULL 				comment '区域名',
  `area_code` 		varchar(8) 		DEFAULT NULL 				comment '区域编码',
  `parent_id`     	int(11)        	DEFAULT NULL               	comment '父级id',
  `area_rank`     	int(2)        	DEFAULT NULL               	comment '级别 0:省,1:市,2:区域',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '区域信息表';

-- ----------------------------
-- 8、系统参数表
-- ----------------------------
drop table if exists t_sys_config;
create table t_sys_config (
	id 		   		   int(11) 	     not null auto_increment    comment '参数主键',
	config_name        varchar(64)   default null               comment '参数名称',
	config_key         varchar(32)   default null               comment '参数键名',
	config_value       varchar(32)   default null               comment '参数键值',
	config_type        int(1)    	 default 0                	comment '系统内置（0否 1是）',
    create_by          varchar(64)   default null               comment '创建者',
    create_time 	   datetime                                 comment '创建时间',
    update_by          varchar(64)   default null               comment '更新者',
    update_time        datetime                                 comment '更新时间',
	remark 	           varchar(500)  default null 				comment '备注',
	primary key (id),
	unique (config_key)
) engine=innodb auto_increment=1 default charset=utf8 comment = '参数配置表';

-- ----------------------------
-- 初始化- 系统参数数据
-- ----------------------------


-- ----------------------------
-- 9、定时任务调度表
-- ----------------------------
drop table if exists t_sys_job;
create table t_sys_job (
  id 		      	  int(11) 	    not null auto_increment    comment '任务ID',
  job_name            varchar(64)   default ''                 comment '任务名称',
  job_group           varchar(64)   default ''                 comment '任务组名',
  method_name         varchar(200)  default ''                 comment '任务方法',
  params              varchar(200)  default ''                 comment '方法参数',
  cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
  status              int(2)    	default 0                  comment '状态（0正常 1暂停）',
  create_by           varchar(64)   default ''                 comment '创建者',
  create_time         datetime                                 comment '创建时间',
  update_by           varchar(64)   default ''                 comment '更新者',
  update_time         datetime                                 comment '更新时间',
  remark              varchar(500)  default ''                 comment '备注信息',
  primary key (id),
  unique(job_name, job_group,method_name)
) engine=innodb auto_increment=1 default charset=utf8 comment = '定时任务调度表';


-- ----------------------------
-- 10、定时任务调度日志表
-- ----------------------------
drop table if exists t_sys_job_log;
create table t_sys_job_log (
  id		          int(11) 	    not null auto_increment    comment '任务日志ID',
  job_name            varchar(64)   not null                   comment '任务名称',
  job_group           varchar(64)   not null                   comment '任务组名',
  method_name         varchar(500)                             comment '任务方法',
  params              varchar(200)  default ''                 comment '方法参数',
  job_message         varchar(500)                             comment '日志信息',
  status              int(2)	    default 0                  comment '执行状态（0正常 1失败）',
  exception_info      text                                     comment '异常信息',
  create_time         datetime                                 comment '创建时间',
  primary key (id)
) engine=innodb auto_increment=1 charset=utf8 comment = '定时任务调度日志表';



-- ----------------------------
-- 11、序列号策略表
-- ----------------------------
drop table if exists t_sys_serial_number;
create table t_sys_serial_number(
	id          	 	int(11) 		 NOT NULL auto_increment    comment 'id',
	module_name			varchar(32)		 default null				comment '模块名称',
	module_code			varchar(32)      default null				comment '模块编码',
	config_templet		varchar(512)     default null				comment '使用的序列号模板',
	cur_serial			int(11)		 	 default null				comment '当前序列号',
	pre_max_num			int(11)			 default null				comment '预生成序列号存放在缓存中的个数',
	is_auto_increment	int(2)			 default null				comment '是否自增长 0否 1是',
	primary key (id),
	unique key (module_code)
) engine=innodb auto_increment=1 default charset=utf8 comment = '序列号策略表';


-- ----------------------------
-- 初始化- 序列号数据
-- ----------------------------
insert into t_sys_serial_number VALUES (1,'系统图模块','sys_diagram_struct','${number}',0,100,1);


-- ----------------------------
-- 12、数据字典类型
-- ----------------------------
drop table if exists t_sys_dict_type;
create table t_sys_dict_type
(
	id          	 int(11) 		 not null auto_increment    comment '字典主键',
	dict_name        varchar(128)    default null               comment '字典名称',
	dict_type        varchar(128)    default null               comment '字典类型',
    status 			 int(2) 		 default 0			    	comment '状态（0正常 1停用）',
    create_by        varchar(64)     default null               comment '创建者',
    create_time      datetime                                   comment '创建时间',
    update_by        varchar(64) 	 default null		        comment '更新者',
	update_time      datetime                                   comment '更新时间',
    remark 	         varchar(500) 	 default null				comment '备注',
	primary key (id),
	unique (dict_type)
) engine=innodb auto_increment=1 default charset=utf8 comment = '字典类型表';


-- ----------------------------
-- 初始化-数据字典类型表数据
-- ----------------------------



-- ----------------------------
-- 13、字典数据表
-- ----------------------------
drop table if exists t_sys_dict_data;
create table t_sys_dict_data
(
	id        		 int(11) 		 not null auto_increment      comment '字典编码',
	dict_sort        int(4)          default null                 comment '字典排序',
	dict_label       varchar(128)    default null                 comment '字典名称',
	dict_value       varchar(128)    default null                 comment '字典键值',
	dict_type        varchar(128)    default null                 comment '字典类型',
	dict_rank        int(11)    	 default null                 comment '所属级别',
	parent_id        int(11)    	 default null                 comment '父字典数据id',
	is_default       int(2)		     default null              	  comment '是否系统默认（0否 1是）',
    status 			 int(2)		 	 default null			      comment '状态（0正常 1停用）',
    create_by        varchar(64)     default null                 comment '创建者',
    create_time      datetime                                     comment '创建时间',
    update_by        varchar(64) 	 default null			      comment '更新者',
	update_time      datetime                                     comment '更新时间',
    remark 	         varchar(500) 	 default null 				  comment '备注',
	primary key (id),
	KEY `idx_dict_type` (`dict_type`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '字典数据表';

-- ----------------------------
-- 初始化-字典数据表数据
-- ----------------------------




-- ----------------------------
-- 14、管辖区信息表
-- ----------------------------
drop table if exists `t_precinct`;
create table `t_precinct` (
  `id` 				int(11) 		not null auto_increment    	comment 'ID',
  `precinct_name` 	varchar(64) 	DEFAULT NULL 				comment '管辖区名称',
  `duty_name` 		varchar(32) 	DEFAULT NULL 				comment '负责人',
  `duty_phone`     	varchar(32)     DEFAULT NULL               	comment '负责人电话',
  `area_id` 		int(11) 		DEFAULT NULL 				COMMENT '区域id',
  `province_id` 	int(11) 		DEFAULT NULL 				COMMENT '省份id',
  `city_id` 		int(11) 		DEFAULT NULL 				COMMENT '城市id',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '管辖区信息';



-- ----------------------------
-- 16、用户管辖区关联表
-- ----------------------------
drop table if exists `t_user_precinct`;
create table `t_user_precinct` (
  `id` 				int(11) 		not null auto_increment    	comment 'ID',
  `precinct_id` 	int(11) 		DEFAULT NULL 				comment '管辖区id',
  `uid` 			int(11) 		DEFAULT NULL 				comment '用户id',
  primary key (`id`),
  KEY `idx_user_precinct_ids` (`precinct_id`,`uid`) USING BTREE
) engine=innodb auto_increment=1 default charset=utf8 comment = '用户管辖区';



-- ----------------------------
-- 17、单位信息表
-- ----------------------------
drop table if exists `t_unit`;
create table `t_unit` (
  `id` 				int(11) 		not null auto_increment    	comment 'ID',
  `unit_name` 		varchar(64) 	DEFAULT NULL 				comment '单位名称',
  `area_id` 		int(11) 		DEFAULT NULL 				COMMENT '区域id',
  `province_id` 	int(11) 		DEFAULT NULL 				COMMENT '省份id',
  `city_id` 		int(11) 		DEFAULT NULL 				COMMENT '城市id',
  `precinct_id` 	int(11) 		DEFAULT NULL 				COMMENT '管辖区id',
  `address`     	varchar(512)    DEFAULT NULL               	comment '单位地址',
  `business_scope` 	varchar(512) 	DEFAULT NULL 				COMMENT '经营范围',
  `unit_type` 		int(2) 			DEFAULT NULL 				COMMENT '单位类型',
  `supervise_level` int(2) 			DEFAULT NULL 				COMMENT '监管等级：1:一级,2:二级,3:三级',
  `transactor` 		varchar(32) 	DEFAULT NULL 				COMMENT '经营人',
  `economy_system` 	int(2)		 	DEFAULT NULL 				COMMENT '经济所有制 1:私营经济、2:个体经济、3:外资经济、4:混合经济、5:国有经济、6:集体经济、7:混合所有制经济',
  `id_card` 		varchar(32) 	DEFAULT NULL 				COMMENT '身份证号码',
  `employee_num` 	int(4) 			DEFAULT NULL 				COMMENT '职工人数',
  `contact_phone`   varchar(16)     DEFAULT NULL               	comment '联系电话',
  `join_time` 		datetime 		DEFAULT NULL 				COMMENT '接入时间',
  `items`   		varchar(512)    DEFAULT NULL               	comment '物品存储',
  `lng` 			decimal(10,6) 	DEFAULT NULL 				COMMENT '经度',
  `lat` 			decimal(10,6) 	DEFAULT NULL 				COMMENT '纬度',
  `unit_pic`   		varchar(512)    DEFAULT NULL               	comment '单位图片',
  `iot_status` 		int(2) 			DEFAULT NULL 				COMMENT '物联状态（0在线 1离线）',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '单位信息';



-- ----------------------------
-- 18、消防设施信息表
-- ----------------------------
drop table if exists `t_facilities`;
create table `t_facilities` (
  `id` 				int(11) 		not null auto_increment    	comment 'ID',
  `unit_id` 		int(11) 		DEFAULT NULL 				COMMENT '单位id',
  `precinct_id` 	int(11) 		DEFAULT NULL 				COMMENT '管辖区id',
  `fa_name` 		varchar(64) 	DEFAULT NULL 				comment '设施名称',
  `fa_system_id` 	int(11) 		DEFAULT NULL 				COMMENT '设施系统类型',
  `fa_type_id` 		int(11) 		DEFAULT NULL 				COMMENT '设施型号',
  `fa_number`     	int(11)    		DEFAULT NULL               	comment '设施数量',
  `use_date` 		date 			DEFAULT NULL 				COMMENT '投入使用时间',
  `unit_name` 		varchar(64)		DEFAULT NULL 				COMMENT '生产单位名称',
  `unit_phone` 		varchar(16)		DEFAULT NULL 				COMMENT '生产单位电话',
  `maintain_name` 	varchar(64) 	DEFAULT NULL 				COMMENT '维修保养单位',
  `maintain_phone` 	varchar(16) 	DEFAULT NULL 				COMMENT '维修保养电话',
  `service_status` 	int(2) 			DEFAULT NULL 				COMMENT '服务状态（0有效 1无效）',
  `upstatus_time` 	datetime 		DEFAULT NULL 				COMMENT '服务状态变化时间',
  `camera_name` 	varchar(64) 	DEFAULT NULL 				COMMENT '绑定摄像头名称',
  `camera_address` 	varchar(256) 	DEFAULT NULL 				COMMENT '摄像头拍摄位置',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '消防设施信息';



-- ----------------------------
-- 19、监测数据
-- ----------------------------
drop table if exists `t_diagram_struct`;
create table `t_diagram_struct` (
  `id` 				int(11) 		not null 			    	comment 'ID',
  `parent_id` 		int(11) 		DEFAULT NULL 				comment '父级id',
  `struct_type` 	int(2) 			DEFAULT NULL 				COMMENT '数据类型 1:遥控数据 2:开关配置 3:设施监测',
  `struct_name` 	varchar(64)		DEFAULT NULL 				COMMENT '数据名称',
  `struct_desc`     varchar(64)		DEFAULT NULL 				COMMENT '数据描述',
  `unit_id` 		int(11) 		DEFAULT NULL 				COMMENT '单位id',
  `precinct_id` 	int(11) 		DEFAULT NULL 				COMMENT '管辖区id',
  `facilities_id` 	int(11) 		DEFAULT NULL 				comment '设施id',
  `fa_system_id` 	int(11) 		DEFAULT NULL 				COMMENT '设施系统类型',
  `struct_address` 	varchar(64)		DEFAULT NULL 				COMMENT '地址(编号)',
  `struct_pic` 		varchar(512)	DEFAULT NULL 				COMMENT '图片路径',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '监测数据';


-- ----------------------------
-- 20、监测数据项
-- ----------------------------
drop table if exists `t_diagram_item`;
create table `t_diagram_item` (
  `id` 				int(11) 		not null auto_increment    	comment 'ID',
  `ds_id` 			int(11) 		DEFAULT NULL 				comment '数据(t_diagram_struct)id',
  `item_id` 		varchar(32)		DEFAULT NULL 				COMMENT '数据项id',
  `display` 		int(2)			DEFAULT NULL 				COMMENT '是否显示 0:否  1:是',
  `item_type` 		int(4) 			DEFAULT NULL 				COMMENT '数据项类型 1:数据项 2:告警',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '监测数据项';



-- ----------------------------
-- 21、单位视频参数
-- ----------------------------
drop table if exists `t_unit_camera`;
create table `t_unit_camera` (
  `id` 				int(11) 		not null auto_increment    	comment 'ID',
  `unit_id` 		int(11) 		DEFAULT NULL 				COMMENT '单位id',
  `precinct_id` 	int(11) 		DEFAULT NULL 				COMMENT '管辖区id',
  `monitor_name` 	varchar(64)		DEFAULT NULL 				COMMENT '监控点名称',
  `important_site` 	varchar(128)	DEFAULT NULL 				COMMENT '重要位置',
  `app_key` 		varchar(128)	DEFAULT NULL 				COMMENT 'appKey',
  `app_secret` 		varchar(128)	DEFAULT NULL 				COMMENT 'appSecret',
  `camera_name` 	varchar(32)		DEFAULT NULL 				COMMENT '视频名称',
  `device_serial` 	varchar(64)		DEFAULT NULL 				COMMENT '设备序列号',
  `channel_no` 		int(11) 		DEFAULT NULL 				COMMENT '通道号',
  `camera_url` 		varchar(128)	DEFAULT NULL 				COMMENT '直播链接',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '单位视频参数';


-- ----------------------------
-- 22、单位巡查记录
-- ----------------------------
drop table if exists `t_unit_patrol`;
create table `t_unit_patrol` (
  `id` 				int(11) 		not null auto_increment    	comment 'ID',
  `unit_id` 		int(11) 		DEFAULT NULL 				COMMENT '单位id',
  `precinct_id` 	int(11) 		DEFAULT NULL 				COMMENT '管辖区id',
  `uid` 			int(11) 		DEFAULT NULL 				COMMENT '巡查人',
  `patrol_result` 	int(2) 			DEFAULT NULL 				COMMENT '巡查结果 (0:正常,1:异常)',
  `start_time` 		datetime 		DEFAULT NULL 				COMMENT '发起时间',
  `end_time` 		datetime 		DEFAULT NULL 				COMMENT '结束',
  `precinct_name` 	varchar(64)		DEFAULT NULL 				COMMENT '查岗单位(管辖区名称)',
  `patrol_pic` 		varchar(512)	DEFAULT NULL 				COMMENT '视频截图',
  `status` 			int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '单位巡查记录';



-- ----------------------------
-- 23、
-- ----------------------------
drop table if exists t_load;
CREATE TABLE `t_load` (
  `id` 				int(11) 		NOT NULL AUTO_INCREMENT,
  `unit_id` 		int(11) 		NOT NULL 					COMMENT '单位id',
  `name` 			varchar(32) 	DEFAULT NULL 				COMMENT '名称',
  `capacity` 		int(11) 		DEFAULT NULL 				COMMENT '容量',
  `load_id` 		varchar(32) 	DEFAULT NULL 				COMMENT '负载',
  `energy_id` 		varchar(32) 	DEFAULT NULL 			    COMMENT '能耗',
  `power_factor_id` varchar(32) 	DEFAULT NULL 				COMMENT '功率因数',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  PRIMARY KEY (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '';



-- ----------------------------
-- 24、
-- ----------------------------
drop table if exists t_load_max;
CREATE TABLE `t_load_max`(
  `id` 				int(11) 	    NOT NULL AUTO_INCREMENT,
  `item_id` 		varchar(32) 	NOT NULL 					COMMENT '负载数据项id',
  `cur_date` 		varchar(32) 	DEFAULT NULL 				COMMENT '时间',
  `max_value` 		float 			DEFAULT NULL 				COMMENT '最大值',
  `type` 			int(2) 			DEFAULT NULL 				COMMENT '类型 1:天，2:月，3:年',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  PRIMARY KEY (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '';



-- ----------------------------
-- 25、警情表
-- ----------------------------
drop table if exists `t_facilities_alarm`;
create table `t_facilities_alarm` (
  `id` 					int(11) 		not null auto_increment    	comment 'ID',
  `unit_id` 			int(11) 		DEFAULT NULL 				COMMENT '单位id',
  `struct_address` 		varchar(64)		DEFAULT NULL 				COMMENT '地址(编号)',
  `facilities_id` 		int(11) 		DEFAULT NULL 				comment '设施id',
  `alarm_id`			int(11)			default null				comment '告警id',
  `alarm_item_id`		varchar(32)		default null				comment '告警数据项id',
  alarm_content			varchar(512)	default null				comment '告警内容',
  alarm_level			int(4)		 	default null				comment '告警等级',
  alarm_status			int(4)		 	default null				comment '告警状态',
  alarm_type			int(4)		 	default null				comment '告警类型',
  alarm_time			datetime		default null				comment '告警时间',
  alarm_end_time		datetime		default null				comment '告警结束时间',
  alarm_way				int(2)          default null				comment '告警方式 0:自动 1:人工',
  `handle_time` 		datetime 		DEFAULT NULL 				COMMENT '受理时间',
  `handle_end_time` 	datetime 		DEFAULT NULL 				COMMENT '受理结束时间',
  `handle_status` 		int(4)		 	default null				comment '当前状态 0:未处理、1:已响应、2:处理中、3:已处理',
  `handle_result` 		varchar(512)	default null				comment '处理结果',
  `handle_uid` 			int(11)			default null				comment '处理人',
  misreport				int(2)		 	default null				comment '是否误报 1:误报 2:有效 3:其它',
  alarm_reason			varchar(512)	default null				comment '起火原因',
  alarm_area			varchar(32)		default null				comment '起火面积',
  die_num				varchar(32)		default null				comment	'死亡人数',
  injured				varchar(32)		default null				comment	'受伤人数',
  property_loss			varchar(32)		default null				comment	'财产损失',
  emedial_measures		varchar(512)	default null				comment	'补救概况',
  up_core_time			datetime 		DEFAULT NULL 				COMMENT '向消防通信指挥中心报告时间',
  core_confirm_time		datetime 		DEFAULT NULL 				COMMENT '消防通信指挥中心反馈确认时间',
  core_handle_uname		varchar(32) 	DEFAULT NULL 				COMMENT '消防通信指挥中心受理人姓名',
  core_handle_result	varchar(512) 	DEFAULT NULL 				COMMENT '消防通信指挥中心接管处理情况',
  `status` 				int(2) 			DEFAULT NULL 				COMMENT '状态（0正常 1停用）',
  `create_time` 		datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 		datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 			varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 			varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  primary key (`id`),
  unique key (alarm_id)
) engine=innodb auto_increment=1 default charset=utf8 comment = '警情表';


-- ----------------------------
-- 26、单位网关关联关系
-- ----------------------------
drop table if exists t_unit_device;
CREATE TABLE `t_unit_device`(
  `id` 				int(11) 	    NOT NULL AUTO_INCREMENT,
  `device_id` 		varchar(32) 	DEFAULT NULL 				COMMENT '网关id',
  `unit_id` 		int(11) 		DEFAULT NULL 				COMMENT '单位id',
  `create_time` 	datetime 		DEFAULT NULL 				COMMENT '创建时间',
  `update_time` 	datetime 		DEFAULT NULL 				COMMENT '更新时间',
  `create_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '创建者',
  `update_by` 		varchar(32) 	DEFAULT NULL 				COMMENT '更新者',
  PRIMARY KEY (`id`)
) engine=innodb auto_increment=1 default charset=utf8 comment = '';
