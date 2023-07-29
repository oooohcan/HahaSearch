package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = 6678399474623536960L;
    private Integer userId;
    private String userAccount;
    private String userName;
    private String tenantCode;
    private Integer searchStatus;
    private String avatarUrl;

}
