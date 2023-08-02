package edu.zuel.hahasearch.service;

import edu.zuel.hahasearch.model.domain.Template;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.zuel.hahasearch.model.domain.User;

import java.util.List;

/**
* @author oooohcan
* @description 针对表【template(template 模板表)】的数据库操作Service
* @createDate 2023-07-18 15:31:50
*/
public interface TemplateService extends IService<Template> {
    /**
     * 增加模板
     * @param button
     * @param searchs 高级搜索权限json列表
     * @param color
     * @param style
     * @param isPublic 0-不公开，1-公开
     * @param loginUser
     * @return long
     */
    long addTemplate(Integer button, String searchs, String color, String style, Integer isPublic, User loginUser);

    /**
     * 删除模板
     * @param id
     * @param loginUser
     * @return boolean
     */
    boolean deleteTemplate(long id, User loginUser);

    /**
     * 修改模板
     * @param template
     * @param loginUser
     * @return int
     */
    int updateTemplate(Template template,User loginUser);

    /**
     * 展示模板
     * @param userAccount
     * @return  模板列表
     */
    List<Template> listTemplate(String userAccount);

}
