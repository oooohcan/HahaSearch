package edu.zuel.hahasearch.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author oooohcan
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -4834199715568118548L;
    private String userAccount;
    private String userPassword;
}

