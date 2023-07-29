package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class TemplateUpdateRequest implements Serializable {
    private static final long serialVersionUID = -3211634402438069301L;
    private Integer templateId;
    private Integer button;
    private String searchs;
    private String color;
    private String style;
}
