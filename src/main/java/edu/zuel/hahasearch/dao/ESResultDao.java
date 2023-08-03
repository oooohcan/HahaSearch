package edu.zuel.hahasearch.dao;

import edu.zuel.hahasearch.model.domain.ESResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ESResultDao extends ElasticsearchRepository<ESResult,Integer> {
    List<ESResult> findESResultByTitle(String title);
    List<ESResult> searchByTitle(String title);
    List<ESResult> searchByContent(String content);
    List<ESResult> findESResultByTitleOrContent(String title,String content);
}
