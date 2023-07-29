package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateSearchRequest implements Serializable {
    private static final long serialVersionUID = 7568922737220015414L;
    private Integer searchStatus;
    private Integer userId;
}
