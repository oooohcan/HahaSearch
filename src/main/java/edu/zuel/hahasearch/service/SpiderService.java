package edu.zuel.hahasearch.service;

import edu.zuel.hahasearch.model.domain.Spider;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author SydZh
 * @description 针对表【spider(spider 爬虫任务表)】的数据库操作Service
 * @createDate 2023-07-18 14:22:27
 */
public interface SpiderService extends IService<Spider> {
    String httpSpider(String target, String name, String index, String code, String deep);

    String pauseTask(String code, String index);

    String cancelTask(String code, String index);

    String resumeTask(String code, String index);

    String getTasks(String code);

    String getRunningTasks(String code);

    String getWaitingTasks(String code);
}
