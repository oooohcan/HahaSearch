package edu.zuel.hahasearch.controller;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.zuel.hahasearch.common.BaseResponse;
import edu.zuel.hahasearch.common.ErrorCode;
import edu.zuel.hahasearch.common.ResultUtils;
import edu.zuel.hahasearch.exception.BusinessException;
import edu.zuel.hahasearch.model.domain.Template;
import edu.zuel.hahasearch.model.domain.User;
import edu.zuel.hahasearch.service.TemplateService;
import edu.zuel.hahasearch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/template")
@Slf4j
public class TemplateController {
    @Resource
    private TemplateService templateService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addTemplate(Integer button, String searchs, String color, String style, Integer isPublic, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        long result = templateService.addTemplate(button,searchs,color, style,isPublic,loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateTemplate(@RequestBody Template template,HttpServletRequest request){
        if(template == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        int result = templateService.updateTemplate(template,loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTemplate(long id, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        boolean result = templateService.deleteTemplate(id, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/list")
    public BaseResponse<Page<Template>> listTemplate(long current, long size, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        if(!userService.isAdmin(loginUser)){
            queryWrapper.eq("user_account",loginUser.getUserAccount()).or().eq("is_public",1);
        }
        Page<Template> templatePage = templateService.page(new Page<>(current,size),queryWrapper);
        return ResultUtils.success(templatePage);
    }

    @PostMapping("/share")
    public BaseResponse<Integer> shareTemplate(Integer templateId,Integer isPublic,HttpServletRequest request){
        // 1、校验
        if(templateId <= 0) throw new BusinessException(ErrorCode.PARAMS_ERROR,"模板ID小于0");
        User loginUser = userService.getLoginUser(request);
        if(!userService.isAdmin(loginUser)) throw new BusinessException(ErrorCode.NO_AUTH);
        if(!isPublic.equals(0) && !isPublic.equals(1)) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        // 2、修改模板共享权限
        Template template = new Template();
        template.setId(templateId);
        template.setIsPublic(isPublic);
        template.setUserAccount(loginUser.getUserAccount());
        int result = templateService.updateTemplate(template,loginUser);
        return ResultUtils.success(result);
    }
}
