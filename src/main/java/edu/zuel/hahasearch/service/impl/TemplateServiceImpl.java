package edu.zuel.hahasearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zuel.hahasearch.model.domain.Template;
import edu.zuel.hahasearch.service.TemplateService;
import edu.zuel.hahasearch.mapper.TemplateMapper;
import org.springframework.stereotype.Service;

/**
* @author SydZh
* @description 针对表【template(template 模板表)】的数据库操作Service实现
* @createDate 2023-07-18 15:31:50
*/
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template>
    implements TemplateService{

}




