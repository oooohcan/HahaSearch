package edu.zuel.hahasearch.dao;
import java.util.ArrayList;
import java.util.Date;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import edu.zuel.hahasearch.model.domain.ESResult;
import edu.zuel.hahasearch.service.ESResultService;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;

@SpringBootTest
public class ESResultTest {
    @Autowired
    private ESResultRepository esResultRepository;

    @Autowired
    private ESResultService esResultService;

    @Autowired
    private ElasticsearchOperations esOperations;

    @Test
    public void createIndex(){
        //创建索引，系统初始化会自动创建索引
        System.out.println("创建索引");
    }


//    这个测不了
//    @Test
//    public void deleteIndex(){
//        elasticsearchRestTemplate.delete(ESResult.class);
//        System.out.println("删除索引");
//    }

    @Test
    public void addOne(){
        ESResult esResult = new ESResult();
        esResult.setTitle("保存单个的测试");
        esResult.setContent("aaaahhhhhahaha");
        esResult.setWebsite("www.x.com");
        esResult.setDate(new Date());
        esResult.setTenantCode("zuel");
        //调用service层
        int i = esResultService.saveESResult(esResult);
        System.out.println("service保存结果：" + i);
        //调用dao层
        //ESResult save = esResultRepository.save(esResult);
        //System.out.println("repository保存对象：" + save );
    }



    @Test
    public void addBatch(){
        List<ESResult> esResultList = new ArrayList<>();
        for(int i = 1; i <= 100; i++){
            ESResult esResult = new ESResult();
            esResult.setTitle("hhh"+i);
            esResult.setContent("hhh内容多搜索"+i);
            esResult.setWebsite("www.baidu.com");
            esResult.setImgUrl("https://img2.woyaogexing.com/2023/08/02/95eb842745c44516d3b33b96a1a16be1.jpg");
            esResult.setType("hhh");
            esResult.setTenantCode("zuel");
            esResultList.add(esResult);
        }
        //调用dao层
        Iterable<ESResult> esResults = esResultRepository.saveAll(esResultList);
        System.out.println("保存对象："+esResults);
    }

    @Test
    public void addBatchService(){
        List<ESResult> esResultList = new ArrayList<>();
        for(int i = 1; i <= 100; i++){
            ESResult esResult = new ESResult();
            esResult.setTitle("hhh"+i);
            esResult.setContent("hhh内容"+i);
            esResult.setWebsite("www.hhh.com");
            esResult.setImgUrl("https://img2.woyaogexing.com/2023/08/02/95eb842745c44516d3b33b96a1a16be1.jpg");
            esResult.setType("test");
            esResult.setTenantCode("zuel");
            esResult.setDate(new Date());
            esResultList.add(esResult);
        }
        // 调用service层
        int save = esResultService.saveBatchESResult(esResultList);
        System.out.println("返回结果"+save);
    }

    /**
     * 不带参数直接列出全部
     */
    @Test
    public void findAll(){
        Iterable<ESResult> esResultList = esResultRepository.findAll();
        esResultList.forEach(System.out::println);
    }

    /**
     * 匹配要求词的准确度较高
     */
    @Test
    public void findByX(){
        Page<ESResult> esResultList = esResultRepository.findESResultByTitleOrContent("hhh","aaa", PageRequest.of(0,10));
        System.out.println(esResultList.getTotalElements());
        esResultList.forEach(System.out::println);
    }

    /**
     * 匹配要求词的准确度较高
     */
    @Test
    public void searchByX(){
        Page<ESResult> esResultList = esResultRepository.searchESResultByTitleOrContent("hhh","aaa", PageRequest.of(0,10));
        esResultList.forEach(System.out::println);
    }

    /**
     * 可模糊搜索
     */
    @Test
    public void searchLikeX(){
        Page<ESResult> esResultList = esResultRepository.searchESResultByTitleLikeOrContentLike("hhh","aaa", PageRequest.of(0,10));
        esResultList.forEach(System.out::println);
    }
//    这个方式跑不通
//    @Test
//    public void searchMatchX(){
//        String keyword = "hh";
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("content",keyword);
//        SearchHits<ESResult> search = esOperations.search((Query) matchQueryBuilder,ESResult.class);
//        search.forEach(System.out::println);
//    }

}
