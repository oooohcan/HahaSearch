package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -3196401679292778718L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String tenantCode;
}
