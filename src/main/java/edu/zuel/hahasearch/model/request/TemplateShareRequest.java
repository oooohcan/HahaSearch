package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TemplateShareRequest implements Serializable {
    private static final long serialVersionUID = -7012852368079767761L;
    private Integer templateId;
    private Integer isPublic;
}
