package edu.zuel.hahasearch.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * spider 爬虫任务表
 *
 * @TableName spider
 */
@TableName(value = "spider")
@Data
public class Spider implements Serializable {
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
     * tenant_code 租户码
     */
    private String tenantCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Spider() {
        this.isDeleted = 0;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public Spider(String tenantCode) {
        this();
        this.tenantCode = tenantCode;
    }

}