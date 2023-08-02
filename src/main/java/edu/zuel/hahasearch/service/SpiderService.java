package edu.zuel.hahasearch.service;

import edu.zuel.hahasearch.model.domain.Spider;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.zuel.hahasearch.model.domain.Task;

import java.util.List;

/**
* @author oooohcan
* @description 针对表【spider(spider 爬虫任务表)】的数据库操作Service
* @createDate 2023-07-18 14:22:27
*/
public interface SpiderService extends IService<Spider> {
    String httpSpider(String target, String name, String code, String deep);

    String pauseTask(String code, String index);

    String cancelTask(String code, String index);

    String resumeTask(String code, String index);

    List<Task> getTasks(String code);

    List<Task> getRunningTasks(String code);

    List<Task> getWaitingTasks(String code);
}
