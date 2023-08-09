package edu.zuel.hahasearch.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.zuel.hahasearch.common.BaseResponse;
import edu.zuel.hahasearch.common.ErrorCode;
import edu.zuel.hahasearch.common.ResultUtils;
import edu.zuel.hahasearch.exception.BusinessException;
import edu.zuel.hahasearch.model.domain.Template;
import edu.zuel.hahasearch.model.domain.User;
import edu.zuel.hahasearch.model.request.TemplateAddRequest;
import edu.zuel.hahasearch.model.request.TemplateShareRequest;
import edu.zuel.hahasearch.model.request.TemplateUpdateRequest;
import edu.zuel.hahasearch.service.TemplateService;
import edu.zuel.hahasearch.service.UserService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/template")
@Slf4j
public class TemplateController {
    @Resource
    private TemplateService templateService;

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/add")
    public BaseResponse<Long> addTemplate(@RequestBody TemplateAddRequest templateAddRequest, HttpServletRequest request){
        if(templateAddRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        Integer button = templateAddRequest.getButton();
        String searchs = templateAddRequest.getSearchs();
        String color = templateAddRequest.getColor();
        String style = templateAddRequest.getStyle();
        Integer isPublic = templateAddRequest.getIsPublic();
        long result = templateService.addTemplate(button,searchs,color, style,isPublic,loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateTemplate(@RequestBody TemplateUpdateRequest templateUpdateRequest, HttpServletRequest request){
        if(templateUpdateRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        // 建立编辑对象
        Template template = new Template();
        template.setId(templateUpdateRequest.getTemplateId());
        template.setColor(templateUpdateRequest.getColor());
        template.setStyle(templateUpdateRequest.getStyle());
        template.setButton(templateUpdateRequest.getButton());
        template.setSearchs(templateUpdateRequest.getSearchs());
        template.setUserAccount(loginUser.getUserAccount());
        int result = templateService.updateTemplate(template,loginUser);
        return ResultUtils.success(result);
    }

    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteTemplate(Integer templateId, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        boolean result = templateService.deleteTemplate(templateId, loginUser);
        return ResultUtils.success(result);
    }

    @GetMapping("/list")
    public BaseResponse<Page<Template>> listTemplate(long current, long size, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        // 创建redis键
        String redisKey =String.format("hahasearch:template:list:%s",loginUser.getId());
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 如果有缓存，直接读缓存
        Page<Template> templatePage =(Page<Template>)valueOperations.get(redisKey);
        if(templatePage != null) return ResultUtils.success(templatePage);
        // 无缓存，查数据库
        QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
        if(!userService.isAdmin(loginUser)){
            queryWrapper.eq("user_account",loginUser.getUserAccount()).or().eq("is_public",1);
        }
        templatePage = templateService.page(new Page<>(current,size),queryWrapper);
        // 查完数据库写入redis缓存
        try{
            valueOperations.set(redisKey,templatePage,30000, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            log.error("redis key is error",e);
        }
        return ResultUtils.success(templatePage);
    }

    @PostMapping("/share")
    public BaseResponse<Integer> shareTemplate(@RequestBody TemplateShareRequest templateShareRequest, HttpServletRequest request){
        // 1、校验
        if(templateShareRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        if(!userService.isAdmin(loginUser)) throw new BusinessException(ErrorCode.NO_AUTH);
        // 2、修改模板共享权限
        Template template = new Template();
        template.setId(templateShareRequest.getTemplateId());
        template.setIsPublic(templateShareRequest.getIsPublic());
        template.setUserAccount(loginUser.getUserAccount());
        int result = templateService.updateTemplate(template,loginUser);
        return ResultUtils.success(result);
    }
}
