package edu.zuel.hahasearch.model.domain;

import io.github.classgraph.json.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "esresult",createIndex = true)
public class ESResult {
    @Id
    @Field(type = FieldType.Integer)
    private Integer id;     //搜索结果ID
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;   //标题
    @Field(type = FieldType.Text)
    private String content; //内容
    @Field(type = FieldType.Keyword)
    private String website; //来源网站
    @Field(type = FieldType.Keyword, index = false)
    private String imgUrl;  //图片地址（若有）
    @Field(type = FieldType.Keyword)
    private String type;    //文档类型
    @Field(type = FieldType.Text)
    private String other;   //其他内容
    @Field(type = FieldType.Date,format = DateFormat.date_time)
    private Date date;      //发布日期
}