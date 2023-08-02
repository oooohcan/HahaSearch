package edu.zuel.hahasearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zuel.hahasearch.model.domain.Spider;
import edu.zuel.hahasearch.service.SpiderService;
import edu.zuel.hahasearch.mapper.SpiderMapper;
import org.springframework.stereotype.Service;

/**
* @author oooohcan
* @description 针对表【spider(spider 爬虫任务表)】的数据库操作Service实现
* @createDate 2023-07-18 14:22:27
*/
@Service
public class SpiderServiceImpl extends ServiceImpl<SpiderMapper, Spider>
    implements SpiderService{

}




