package edu.zuel.hahasearch.dao;
import java.util.ArrayList;
import java.util.Date;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import edu.zuel.hahasearch.model.domain.ESResult;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class ESResultDaoTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ESResultDao esResultDao;

    @Test
    public void createIndex(){
        //创建索引，系统初始化会自动创建索引
        System.out.println("创建索引");
    }


//    参数出错
//    @Test
//    public void deleteIndex(){
//        elasticsearchRestTemplate.delete(ESResult.class);
//        System.out.println("删除索引");
//    }

    @Test
    public void addDoc(){
        ESResult esResult = new ESResult();
//        esResult.setId(1);
        esResult.setTitle("hhh1");
        esResult.setContent("内容哈哈hhahah");
        esResult.setWebsite("www.baidu.com");
        esResult.setDate(new Date());
        ESResult save = esResultDao.save(esResult);
        System.out.println("保存对象："+save);
    }

    @Test
    public void addBatchDoc(){
        List<ESResult> esResultList = new ArrayList<>();
        for(int i = 1; i <= 100; i++){
            ESResult esResult = new ESResult();
            esResult.setId(i);
            esResult.setTitle("haha"+i);
            esResult.setContent("内容哈哈haha"+i);
            esResult.setWebsite("www.baidu.com");
            esResult.setImgUrl("https://img2.woyaogexing.com/2023/08/02/95eb842745c44516d3b33b96a1a16be1.jpg");
            esResult.setType("test");
            esResult.setOther("补充信息"+i);
            esResult.setDate(new Date());
            esResultList.add(esResult);
        }
        Iterable<ESResult> esResults = esResultDao.saveAll(esResultList);
        System.out.println("保存对象："+esResults);
    }

    @Test
    public void listDoc(){
        Iterable<ESResult> esResultList = esResultDao.findAll();
        esResultList.forEach(System.out::println);
    }

    @Test
    public void findDoc(){
        List<ESResult> esResultList = esResultDao.findESResultByTitle("haha1");
        System.out.println(esResultList.size());
        esResultList.forEach(System.out::println);
    }

    @Test
    public void searchDoc(){
        List<ESResult> esResultList = esResultDao.searchByContent("内容");
        esResultList.forEach(System.out::println);
    }

}
