package edu.zuel.hahasearch.controller;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.zuel.hahasearch.common.BaseResponse;
import edu.zuel.hahasearch.common.ErrorCode;
import edu.zuel.hahasearch.common.ResultUtils;
import edu.zuel.hahasearch.exception.BusinessException;
import edu.zuel.hahasearch.model.domain.User;
import edu.zuel.hahasearch.model.request.UserLoginRequest;
import edu.zuel.hahasearch.model.request.UserRegisterRequest;
import edu.zuel.hahasearch.model.request.UserUpdateRequest;
import edu.zuel.hahasearch.model.request.UserUpdateSearchRequest;
import edu.zuel.hahasearch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static edu.zuel.hahasearch.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String tenantCode = userRegisterRequest.getTenantCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,tenantCode)){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        long result = userService.userRegister(userAccount,userPassword,checkPassword,tenantCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @GetMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if(request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        //更新用户信息
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 获取所有用户
     * @param current
     * @param size
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<Page<User>> listUsers(long current, long size, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Page<User> userPage = userService.page(new Page<>(current,size));
        Page<User> safetyUserPage = userPage.setRecords(userPage.getRecords().stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList()));
        return ResultUtils.success(safetyUserPage);
    }

    /**
     * 按用户名搜索用户
     * @param username
     * @param current
     * @param size
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<Page<User>> searchUsers(String username, long current, long size,HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("user_name", username);
        }
        Page<User> userPage = userService.page(new Page<>(current,size),queryWrapper);
        Page<User> safetyUserPage = userPage.setRecords(userPage.getRecords().stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList()));
        return ResultUtils.success(safetyUserPage);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // 校验参数是否为空
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 建立用户对象
        User user = new User();
        user.setId(userUpdateRequest.getUserId());
        user.setUserName(userUpdateRequest.getUserName());
        user.setUserAccount(userUpdateRequest.getUserAccount());
        user.setAvatarUrl(userUpdateRequest.getAvatarUrl());
        user.setTenantCode(userUpdateRequest.getTenantCode());
        user.setSearchStatus(userUpdateRequest.getSearchStatus());
        User loginUser = userService.getLoginUser(request);
        int result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 用户权限修改，0-搜索所有，1-禁用高级搜索，2-禁用所有搜索
     * @param searchStatus
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/updateSearch")
    public BaseResponse<Integer> updateSearchStatus(@RequestBody UserUpdateSearchRequest userUpdateSearchRequest, HttpServletRequest request){
        // 1、校验
        if(userUpdateSearchRequest == null) throw new BusinessException(ErrorCode.NULL_ERROR);
        Integer searchStatus = userUpdateSearchRequest.getSearchStatus();
        Integer userId = userUpdateSearchRequest.getUserId();
        if(StringUtils.isAnyBlank(String.valueOf(searchStatus),String.valueOf(userId))){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if( searchStatus < 0 || searchStatus >2){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"权限码错误");
        }
        // 2、创建修改对象
        User newUser = new User();
        newUser.setId(userId);
        newUser.setSearchStatus(searchStatus);
        User loginUser = userService.getLoginUser(request);
        int result = userService.updateUser(newUser, loginUser);
        return ResultUtils.success(result);
    }

    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(Integer userId, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(userId);
        return ResultUtils.success(result);
    }



}
