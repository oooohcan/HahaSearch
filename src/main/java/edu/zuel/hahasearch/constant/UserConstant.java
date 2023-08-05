package edu.zuel.hahasearch.constant;

public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 管理权限 0-普通，1-管理员
     */
    int DEFAULT_ROLE = 0;
    int ADMIN_ROLE = 1;

    /**
     *  搜索权限 0-可搜所有，1-禁用高级，2-禁用所有搜索
     */
    int DEFAULT_STATUS = 0;
    int FOBBID_HIGH_STATUS = 1;
    int FOBBIN_ALL_STATUS = 2;

    /**
     * 租户码，用于租户数据隔离
     */
    String [] TENANT_CODE_LIST = {"zuel","test","hhh"};
}
