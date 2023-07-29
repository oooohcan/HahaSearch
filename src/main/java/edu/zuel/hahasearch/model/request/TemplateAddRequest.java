package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TemplateAddRequest implements Serializable {
    private static final long serialVersionUID = 6230924310882190568L;
    private Integer button;
    private String searchs;
    private String color;
    private String style;
    private Integer isPublic;
}
