package edu.zuel.hahasearch.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zuel.hahasearch.common.ErrorCode;
import edu.zuel.hahasearch.exception.BusinessException;
import edu.zuel.hahasearch.model.domain.Template;
import edu.zuel.hahasearch.model.domain.User;
import edu.zuel.hahasearch.service.TemplateService;
import edu.zuel.hahasearch.mapper.TemplateMapper;
import edu.zuel.hahasearch.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author oooohcan
* @description 针对表【template(template 模板表)】的数据库操作Service实现
* @createDate 2023-07-18 15:31:50
*/
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template>
    implements TemplateService{

    @Resource
    private UserService userService;

    @Resource
    private TemplateMapper templateMapper;

    @Override
    public long addTemplate(Integer button, String searchs, String color, String style, Integer isPublic, User loginUser) {
        // 1、校验
        if(button < 0 || button > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"按钮参数错误");
        }
        if(searchs.length() > 128 || color.length() > 64 || style.length() > 64){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(isPublic != null && !isPublic.equals(0)){
            if(!userService.isAdmin(loginUser)){
                throw new BusinessException(ErrorCode.NO_AUTH, "非管理员不可公开模板");
            }else{
                if(!isPublic.equals(1)) throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        // 2、保存至数据库
        Template template = new Template();
        template.setUserAccount(loginUser.getUserAccount());
        template.setColor(color);
        template.setStyle(style);
        template.setButton(button);
        template.setSearchs(searchs);
        template.setIsPublic(isPublic);
        boolean save = this.save(template);
        if(!save) throw new BusinessException(ErrorCode.PARAMS_ERROR,"数据插入错误");
        return 1;
    }

    @Override
    public boolean deleteTemplate(long id, User loginUser) {
        if(id < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"模板ID小于0");
        }
        // 校验模板用户与登录用户是否一致,用户可删自己的，管理员可删全部
        Template template = templateMapper.selectById(id);
        if(!template.getUserAccount().equals(loginUser.getUserAccount()) && !userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH,"无删除权限");
        }
        return this.removeById(id);
    }

    @Override
    public int updateTemplate(Template template, User loginUser) {
        long templateId = template.getId();
        if(templateId <= 0) throw new BusinessException(ErrorCode.PARAMS_ERROR,"模板ID小于0");
        // 管理员或创建者可修改模板
        if(!template.getUserAccount().equals(loginUser.getUserAccount()) && !userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH,"无编辑权限");
        }
        // isPublic参数限制为 0或1
        Integer isPublic = template.getIsPublic();
        if(!isPublic.equals(0) && !isPublic.equals(1)) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        // 查询模板是否存在
        Template oldTemplate = templateMapper.selectById(templateId);
        if(oldTemplate == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"模板ID错误");
        // 根据模板ID更新
        return templateMapper.updateById(template);
    }

    @Override
    public List<Template> listTemplate(String userAccount) {
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userAccount)){
            templateQueryWrapper.like("user_account",userAccount);
        }
        return this.list(templateQueryWrapper);
    }
}




