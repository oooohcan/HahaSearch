package edu.zuel.hahasearch.service;

import edu.zuel.hahasearch.model.domain.ESResult;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ESResultService {
    int saveBatchESResult(List<ESResult> esResults);
    int saveESResult(ESResult esResult);
    boolean deleteESResult(String id);
    int updateESResult(ESResult esResult);
    Page<ESResult> listESResult(Integer page, Integer size);

}
