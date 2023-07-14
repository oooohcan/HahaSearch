CREATE TABLE tenant(
                       id INT NOT NULL AUTO_INCREMENT  COMMENT 'id 主键' ,
                       is_deleted TINYINT DEFAULT 0  NOT NULL  COMMENT 'is_deleted 是否逻辑删除 0-未删 1-删除' ,
                       create_time DATETIME DEFAULT CURRENT_TIMESTAMP not null COMMENT 'create_time 创建时间' ,
                       update_time DATETIME DEFAULT CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP COMMENT 'update_time 修改时间' ,
                       tenant_name VARCHAR(32) NULL COMMENT 'tenant_name 租户名' ,
                       PRIMARY KEY (id)
) COMMENT = 'tenant 租户表';

CREATE TABLE session(
                        id INT NOT NULL AUTO_INCREMENT  COMMENT 'id 主键' ,
                        is_deleted TINYINT DEFAULT 0  NOT NULL   COMMENT 'is_deleted 是否逻辑删除 0-未删 1-删除' ,
                        create_time DATETIME DEFAULT CURRENT_TIMESTAMP not null COMMENT 'create_time 创建时间' ,
                        update_time DATETIME DEFAULT CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP COMMENT 'update_time 修改时间' ,
                        uuid INT NULL COMMENT 'uuid 用户id' ,
                        token VARCHAR(64)  NULL  COMMENT 'token 登录token' ,
                        PRIMARY KEY (id)
) COMMENT = 'session 登录状态表';
CREATE TABLE user(
                     id INT NOT NULL AUTO_INCREMENT  COMMENT 'id 主键' ,
                     is_deleted TINYINT DEFAULT 0  NOT NULL   COMMENT 'is_deleted 是否逻辑删除 0-未删 1-删除' ,
                     create_time DATETIME DEFAULT CURRENT_TIMESTAMP not null COMMENT 'create_time 创建时间' ,
                     update_time DATETIME DEFAULT CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP COMMENT 'update_time 修改时间' ,
                     user_name VARCHAR(128)  NULL  COMMENT 'user_name 昵称' ,
                     user_account VARCHAR(64)  NULL   COMMENT 'user_account 账户' ,
                     user_pwd VARCHAR(256) NOT NULL   COMMENT 'user_pwd 密码' ,
                     avatar_url VARCHAR(128)  NULL   COMMENT 'avatar_url 头像路径' ,
                     salt VARCHAR(128)  NULL  COMMENT 'salt 加密盐' ,
                     tenant_id INT  NULL  COMMENT 'tenant_id 租户id' ,
                     user_role TINYINT DEFAULT 0 NOT NULL  COMMENT 'user_role 用户角色，0-普通用户，1-管理员' ,
                     search_status TINYINT DEFAULT 0 NOT NULL   COMMENT 'search_status 搜索权限，0-全部，1-禁用高级搜索，2-禁用搜索' ,
                     PRIMARY KEY (id)
) COMMENT = 'user 用户表';

alter table user
    change tenant_id tenant_code varchar(128) null comment 'tenant_code 租户码';