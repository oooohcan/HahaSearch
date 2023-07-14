package edu.zuel.hahasearch.service;

import edu.zuel.hahasearch.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author oooohcan
* @description 针对表【user(user 用户表)】的数据库操作Service
* @createDate 2023-07-06 15:58:26
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @param tenantCode
     * @return 新用户的id
     */
    long userRegister(String userAccount, String userPassword,String checkPassword,String tenantCode);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 脱敏用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户登出
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 搜索用户
     * @param username
     * @param request
     * @return 用户列表
     */
    List<User> searchUser(String username,HttpServletRequest request);

    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    boolean deleteUser(long id,HttpServletRequest request);

    /**
     * 修改用户信息
     * @param user
     * @param loginUser
     * @return
     */
    int updateUser(User user,User loginUser);

    /**
     * 判断当前用户是否为管理员
     * @param loginuser
     * @return
     */
    boolean isAdmin(User loginuser);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏用户
     * @param user
     * @return
     */
    User getSafetyUser(User user);
}
