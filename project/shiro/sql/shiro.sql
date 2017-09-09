/*安全中心相关表*/
CREATE TABLE t_role_permission_rel
(
    n_role_id INT(11) NOT NULL,
    n_permission_id INT(11) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与权限关系表';
CREATE TABLE t_shiro_permission
(
    n_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    c_name VARCHAR(10),
    c_url VARCHAR(255),
    c_identity VARCHAR(16),
    t_createtime TIMESTAMP,
    n_status INT(11) DEFAULT '0'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表/菜单表';
CREATE TABLE t_shiro_role
(
    n_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    c_ids VARCHAR(10) DEFAULT '' NOT NULL COMMENT '角色依赖关系，包含子角色，则用逗号隔开eg:1,2,3',
    c_name VARCHAR(10),
    c_identity VARCHAR(16),
    n_status INT(11) DEFAULT '0',
    t_createtime TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表/组织结构';
CREATE TABLE t_shiro_user
(
    n_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    c_name VARCHAR(20),
    c_password VARCHAR(20),
    n_sex INT(11),
    t_createtime TIMESTAMP,
    c_phone_number VARCHAR(20),
    c_email VARCHAR(100),
    c_address VARCHAR(255),
    n_status INT(11) DEFAULT '0',
    c_number varchar(20) DEFAULT NULL,
    t_birthday TIMESTAMP
)ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表信息';
CREATE TABLE t_user_role_rel
(
    n_user_id INT(11) NOT NULL,
    n_role_id INT(11) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与角色关系表';
CREATE TABLE t_interface_auth (
   n_id int(11) NOT NULL AUTO_INCREMENT,
   c_method_name varchar(255) DEFAULT NULL COMMENT '接口名称',
   c_class_name varchar(255) DEFAULT NULL COMMENT '接口类名',
   b_iSign tinyint(1) DEFAULT '1' COMMENT '是否签名 1是(true) 0否(false)',
   b_isLogin tinyint(1) DEFAULT '1' COMMENT '是否验证登录token 1是(true) 0否(false)',
   t_create_date datetime DEFAULT NULL,
   t_update_date datetime DEFAULT NULL,
  PRIMARY KEY (n_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;