package edu.zuel.hahasearch.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * session 登录状态表
 * @TableName session
 */
@TableName(value ="session")
@Data
public class Session implements Serializable {
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
     * uuid 用户id
     */
    private Integer uuid;

    /**
     * token 登录token
     */
    private String token;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}