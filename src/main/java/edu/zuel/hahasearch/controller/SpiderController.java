package edu.zuel.hahasearch.controller;

import edu.zuel.hahasearch.common.BaseResponse;
import edu.zuel.hahasearch.common.ErrorCode;
import edu.zuel.hahasearch.common.ResultUtils;
import edu.zuel.hahasearch.exception.BusinessException;
import edu.zuel.hahasearch.model.domain.Task;
import edu.zuel.hahasearch.model.domain.User;
import edu.zuel.hahasearch.model.request.*;
import edu.zuel.hahasearch.service.SpiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static edu.zuel.hahasearch.common.ErrorCode.PARAMS_ERROR;
import static edu.zuel.hahasearch.common.ErrorCode.SPIDER_ERROR;
import static edu.zuel.hahasearch.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/spider")
@Slf4j
//@CrossOrigin(origins = {"http://localhost:9095"},allowCredentials = "true")
public class SpiderController {
    @Resource
    private SpiderService spiderService;

    @PostMapping("/http")
    public BaseResponse<String> httpSpider(@RequestBody SpiderHttpRequest spiderHttpRequest, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (spiderHttpRequest == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        String target = spiderHttpRequest.getTarget();
        String name = spiderHttpRequest.getName();
        String code = currentUser.getTenantCode();
        int deep = spiderHttpRequest.getDeep();
        if (target == null || name == null || code == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        try {
            String msg = spiderService.httpSpider(target, name, code, deep);
            return ResultUtils.success(msg);
        } catch (Exception e) {
            return ResultUtils.error(SPIDER_ERROR, e.getMessage(), e.toString());
        }
    }

    @PostMapping("/ftp")
    public BaseResponse<String> ftpSpider(@RequestBody SpiderFtpRequest spiderFtpRequest, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (spiderFtpRequest == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        String target = spiderFtpRequest.getTarget();
        String name = spiderFtpRequest.getName();
        String code = currentUser.getTenantCode();
        String visitDir = spiderFtpRequest.getVisitDir();
        String uname = spiderFtpRequest.getUname();
        String upwd = spiderFtpRequest.getUpwd();
        int deep = spiderFtpRequest.getDeep();
        if (target == null || name == null || code == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        try {
            String msg = spiderService.ftpSpider(target, name, code, visitDir, uname, upwd, deep);
            return ResultUtils.success(msg);
        } catch (Exception e) {
            return ResultUtils.error(SPIDER_ERROR, e.getMessage(), e.toString());
        }
    }

    @PostMapping("/pause_task")
    public BaseResponse<String> pauseTask(@RequestBody SpiderPauseRequest spiderPauseRequest, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        if (spiderPauseRequest == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        String code = currentUser.getTenantCode();
        int index = spiderPauseRequest.getIndex();
        if (code == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        try {
            String msg = spiderService.pauseTask(code, index);
            return ResultUtils.success(msg);
        } catch (Exception e) {
            return ResultUtils.error(SPIDER_ERROR, e.getMessage(), e.toString());
        }
    }

    @PostMapping("/cancel_task")
    public BaseResponse<String> cancelTask(@RequestBody SpiderCancelRequest spiderCancelRequest, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (spiderCancelRequest == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        String code = currentUser.getTenantCode();
        int index = spiderCancelRequest.getIndex();
        if (code == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        try {
            String msg = spiderService.cancelTask(code, index);
            return ResultUtils.success(msg);
        } catch (Exception e) {
            return ResultUtils.error(SPIDER_ERROR, e.getMessage(), e.toString());
        }
    }

    @PostMapping("/resume_task")
    public BaseResponse<String> resumeTask(@RequestBody SpiderResumeRequest spiderResumeRequest, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (spiderResumeRequest == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        String code = currentUser.getTenantCode();
        int index = spiderResumeRequest.getIndex();
        if (code == null) {
            return ResultUtils.error(PARAMS_ERROR);
        }
        try {
            String msg = spiderService.resumeTask(code, index);
            return ResultUtils.success(msg);
        } catch (Exception e) {
            return ResultUtils.error(SPIDER_ERROR, e.getMessage(), e.toString());
        }
    }

    @PostMapping("get_tasks")
    public BaseResponse<List<Task>> getTasks(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String code = currentUser.getTenantCode();
        try {
            return ResultUtils.success(spiderService.getTasks(code));
        } catch (Exception e) {
            return ResultUtils.error(SPIDER_ERROR, e.getMessage(), e.toString());
        }
    }

    @PostMapping("get_running_tasks")
    public BaseResponse<List<Task>> getRunningTasks(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String code = currentUser.getTenantCode();
        try {
            return ResultUtils.success(spiderService.getRunningTasks(code));
        } catch (Exception e) {
            return ResultUtils.error(SPIDER_ERROR, e.getMessage(), e.toString());
        }
    }

    @PostMapping("get_waiting_tasks")
    public BaseResponse<List<Task>> getWaitingTasks(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String code = currentUser.getTenantCode();
        try {
            return ResultUtils.success(spiderService.getWaitingTasks(code));
        } catch (Exception e) {
            return ResultUtils.error(SPIDER_ERROR, e.getMessage(), e.toString());
        }
    }

}
