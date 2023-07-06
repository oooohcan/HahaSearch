package edu.zuel.hahasearch.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * user 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * is_deleted 是否逻辑删除 0-未删 1-删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * create_time 创建时间
     */
    private Date createTime;

    /**
     * update_time 修改时间
     */
    private Date updateTime;

    /**
     * user_name 昵称
     */
    private String userName;

    /**
     * user_account 账户
     */
    private String userAccount;

    /**
     * user_pwd 密码
     */
    private String userPwd;

    /**
     * avatar_url 头像路径
     */
    private String avatarUrl;

    /**
     * salt 加密盐
     */
    private String salt;

    /**
     * tenant_id 租户id
     */
    private Integer tenantId;

    /**
     * user_role 用户角色，0-普通用户，1-管理员
     */
    private Integer userRole;

    /**
     * search_status 搜索权限，0-全部，1-禁用高级搜索，2-禁用搜索
     */
    private Integer searchStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}