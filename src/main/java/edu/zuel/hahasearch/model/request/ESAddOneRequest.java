package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ESAddOneRequest implements Serializable {
    private static final long serialVersionUID = -6006908328249984174L;
    private String title;       //标题
    private String content;     //内容
    private String website;     //来源网站
    private String imgUrl;      //图片地址（若有）
    private String type;        //文档类型
    private String tenantCode;  //租户码
    private Date date;          //发布日期
}
