package edu.zuel.hahasearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zuel.hahasearch.model.domain.Spider;
import edu.zuel.hahasearch.model.domain.Task;
import edu.zuel.hahasearch.service.SpiderService;
import edu.zuel.hahasearch.mapper.SpiderMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author SydZh
 * @description 针对表【spider(spider 爬虫任务表)】的数据库操作Service实现
 * @createDate 2023-07-18 14:22:27
 */
@Service
@Slf4j
public class SpiderServiceImpl extends ServiceImpl<SpiderMapper, Spider>
        implements SpiderService {

    private final String spiderUrl = "http://localhost:11451";
    private final String httpUrl = spiderUrl + "/create_http";
    private final String pauseUrl = spiderUrl + "/pause_task";
    private final String cancelUrl = spiderUrl + "/cancel_task";
    private final String resumeUrl = spiderUrl + "/resume_task";
    private final String getTasksUrl = spiderUrl + "/get_tasks";
    private final String getRunningTasksUrl = spiderUrl + "/get_running_tasks";
    private final String getWaitingTasksUrl = spiderUrl + "/get_waiting_tasks";
    @Resource
    private SpiderMapper spiderMapper;

    private static JSONObject postRequest(String url, JSONObject jsonObject) {
        try {
            HttpURLConnection connection = getHttpUrlConnection(url);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            connection.disconnect();
            return new JSONObject(stringBuilder.toString());
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static HttpURLConnection getHttpUrlConnection(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);

        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "UTF-8");

        connection.connect();
        return connection;
    }

    @Override
    public String httpSpider(String target, String name, String code, String deep) {
        JSONObject jsonObject = new JSONObject();
        Spider spider = new Spider(code);
        spiderMapper.insert(spider);
        int index = spider.getId();
        try {
            jsonObject.put("target", target);
            jsonObject.put("name", name);
            jsonObject.put("index", index);
            jsonObject.put("code", code);
            if (deep != null && !deep.isEmpty()) {
                jsonObject.put("deep", deep);
            }
            JSONObject response = postRequest(httpUrl, jsonObject);
            int responseCode = response.getInt("code");
            if (responseCode != 0) {
                throw new RuntimeException(response.getString("msg"));
            }
            return response.getString("msg");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String pauseTask(String code, String index) {
        return getResult(code, index, pauseUrl);
    }

    @Override
    public String cancelTask(String code, String index) {
        return getResult(code, index, cancelUrl);
    }

    @Override
    public String resumeTask(String code, String index) {
        return getResult(code, index, resumeUrl);
    }

    @Override
    public List<Task> getTasks(String code) {
        return getListTasks(code, getTasksUrl);
    }

    @Override
    public List<Task> getRunningTasks(String code) {
        return getListTasks(code, getRunningTasksUrl);
    }

    @Override
    public List<Task> getWaitingTasks(String code) {
        return getListTasks(code, getWaitingTasksUrl);
    }

    @NotNull
    private List<Task> getListTasks(String code, String getRunningTasksUrl) {
        JSONObject msgObject = getResult(code, getRunningTasksUrl);
        List<Task> tasks = new ArrayList<>();
        if (msgObject == null || msgObject.length() == 0) {
            return tasks;
        }
        try {
            Iterator keys = msgObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                JSONObject taskObject = msgObject.getJSONObject(key);
                Task task = new Task();
                task.setRate(taskObject.getDouble("rate"));
                task.setFail(taskObject.getInt("fail"));
                task.setIndex(taskObject.getInt("index"));
                task.setName(taskObject.getString("name"));
                task.setSuccess(taskObject.getInt("success"));
                task.setUrl(taskObject.getString("url"));
                task.setProperty(taskObject.getString("property"));
                task.setTotal(taskObject.getInt("total"));
                task.setType(taskObject.getString("type"));
                tasks.add(task);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    private String getResult(String code, String index, String resumeUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
            jsonObject.put("index", index);
            JSONObject response = postRequest(resumeUrl, jsonObject);
            int responseCode = response.getInt("code");
            if (responseCode != 0) {
                throw new RuntimeException(response.getString("msg"));
            }
            return response.getString("msg");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject getResult(String code, String getWaitingTasksUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
            JSONObject response = postRequest(getWaitingTasksUrl, jsonObject);
            int responseCode = response.getInt("code");
            if (responseCode != 0) {
                throw new RuntimeException(response.getString("msg"));
            }
            return response.getJSONObject("msg");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}




