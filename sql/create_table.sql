create table user
(
    id            int auto_increment comment 'id 主键'
        primary key,
    is_deleted    tinyint  default 0                 not null comment 'is_deleted 是否逻辑删除 0-未删 1-删除',
    create_time   datetime default CURRENT_TIMESTAMP not null comment 'create_time 创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'update_time 修改时间',
    user_name     varchar(128)                       null comment 'user_name 昵称',
    user_account  varchar(64)                        null comment 'user_account 账户',
    user_pwd      varchar(256)                       not null comment 'user_pwd 密码',
    avatar_url    varchar(128)                       null comment 'avatar_url 头像路径',
    tenant_code   varchar(128)                       null comment 'tenant_code 租户码',
    user_role     tinyint  default 0                 not null comment 'user_role 用户角色，0-普通用户，1-管理员',
    search_status tinyint  default 0                 not null comment 'search_status 搜索权限，0-全部，1-禁用高级搜索，2-禁用搜索'
)
    comment 'user 用户表';

create table spider
(
    id          int auto_increment comment 'id 主键'
        primary key,
    is_deleted  tinyint  default 0                 not null comment 'is_deleted 是否逻辑删除 0-未删 1-删除',
    create_time datetime default CURRENT_TIMESTAMP not null comment 'create_time 创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'update_time 修改时间',
    tenant_code varchar(128)                       null comment 'tenant_code 租户码'
)
    comment 'spider 爬虫任务表';

create table template
(
    id            int auto_increment comment 'id 主键'
        primary key,
    is_deleted    tinyint  default 0                 not null comment 'is_deleted 是否逻辑删除 0-未删 1-删除',
    create_time   datetime default CURRENT_TIMESTAMP not null comment 'create_time 创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment 'update_time 修改时间',
    user_account  varchar(64)                        null comment 'user_account 用户账户',
    color         varchar(64)                        null comment 'color 颜色',
    style         varchar(64)                        null comment 'style 样式',
    button        int                                null comment 'button 按钮类型',
    searchs       varchar(128)                       null comment 'searchs 高级检索类型 json 列表',
    is_public     tinyint  default 0                 not null comment 'is_public 是否公开，0-不公开，1-公开'
)
    comment 'template 模板表';