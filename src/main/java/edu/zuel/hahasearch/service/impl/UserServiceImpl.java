package edu.zuel.hahasearch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zuel.hahasearch.common.ErrorCode;
import edu.zuel.hahasearch.constant.UserConstant;
import edu.zuel.hahasearch.exception.BusinessException;
import edu.zuel.hahasearch.model.domain.User;
import edu.zuel.hahasearch.service.UserService;
import edu.zuel.hahasearch.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static edu.zuel.hahasearch.constant.UserConstant.*;

/**
* @author oooohcan
* @description 针对表【user(user 用户表)】的数据库操作Service实现
* @createDate 2023-07-06 15:58:26
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{


    /**
     * 盐值，混淆原密码
     */
    private static final String SALT = "SydZh";

    @Resource
    private UserMapper userMapper;
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String tenantCode) {
        //1、校验参数
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,tenantCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if(userPassword.length()<8 || checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        if(!ArrayUtils.contains(TENANT_CODE_LIST,tenantCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"无效租户码");
        }
        //校验账户特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userPassword);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }
        //账户不重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = this.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户重复");
        }
        //两次密码相同
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不同");
        }
        //2、加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //3、插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPwd(encryptPassword);
        user.setTenantCode(tenantCode);
        user.setUserName(userAccount);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"数据插入失败");
        }
        return 1;
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1、校验参数
        if(StringUtils.isAnyBlank(userAccount,userPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户或密码为空");
        if(userAccount.length() < 4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度小于4位");
        if(userPassword.length() < 8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度小于8位");
        //校验账户特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userPassword);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户包含特殊字符");
        }
        //2、加密密码后登入
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
        //查询用户是否存，密码是否准确
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_pwd",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if(user == null){
            log.info("user login failed, userAccount can not match userPassword ");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        //3、脱敏用户信息
        User safetyUser = getSafetyUser(user);
        //4、保存登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public List<User> searchUser(String username, HttpServletRequest request) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            userQueryWrapper.like("user_name",username);
        }
        List<User> userList = this.list(userQueryWrapper);
        return userList.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteUser(long id, HttpServletRequest request) {
        if(isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"无管理员权限");
        }
        if(id < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户Id小于0");
        }
        return this.removeById(id);
    }

    @Override
    public int updateUser(User user, User loginUser) {
        long userId = user.getId();
        if(userId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户ID小于0");
        }
        //只有管理员可修改用户
        if(!isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        //查询用户是否存在
        User olduser = userMapper.selectById(userId);
        if(olduser == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户ID错误");
        }
        //用户名不可重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",user.getUserAccount());
        long count = this.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户重复");
        }

        return userMapper.updateById(user);
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    @Override
    public boolean isAdmin(User loginuser) {
        return loginuser != null && loginuser.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if(request == null){
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if(userObj == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return (User) userObj;
    }

    @Override
    public User getSafetyUser(User user) {
        if(user == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setTenantCode(user.getTenantCode());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setSearchStatus(user.getSearchStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUpdateTime(user.getUpdateTime());
        return safetyUser;
    }


}




