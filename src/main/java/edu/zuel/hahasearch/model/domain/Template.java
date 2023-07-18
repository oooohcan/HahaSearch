package edu.zuel.hahasearch.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * template 模板表
 * @TableName template
 */
@TableName(value ="template")
@Data
public class Template implements Serializable {
    /**
     * id 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * is_deleted 是否逻辑删除 0-未删 1-删除
     */
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
     * user_account 用户账户
     */
    private String userAccount;

    /**
     * color 颜色
     */
    private String color;

    /**
     * style 样式
     */
    private String style;

    /**
     * button 按钮类型
     */
    private Integer button;

    /**
     * searchs 高级检索类型 json 列表
     */
    private String searchs;

    /**
     * is_public 是否公开，0-公开，1-不公开
     */
    private Integer isPublic;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}