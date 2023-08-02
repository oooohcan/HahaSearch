package edu.zuel.hahasearch.dao;

import edu.zuel.hahasearch.model.domain.ESResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ESResultDao extends ElasticsearchRepository<ESResult,String> {
    List<ESResult> findESResultByTitle(String title);
    List<ESResult> findESResultByTitleOrContent(String title,String content);
}
