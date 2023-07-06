package edu.zuel.hahasearch.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tenant 租户表
 * @TableName tenant
 */
@TableName(value ="tenant")
@Data
public class Tenant implements Serializable {
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
     * tenant_name 租户名
     */
    private String tenantName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}