package edu.zuel.hahasearch.service.impl;

import edu.zuel.hahasearch.common.ErrorCode;
import edu.zuel.hahasearch.dao.ESResultRepository;
import edu.zuel.hahasearch.exception.BusinessException;
import edu.zuel.hahasearch.model.domain.ESResult;
import edu.zuel.hahasearch.service.ESResultService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ESResultServiceImpl implements ESResultService {
    @Autowired
    private ESResultRepository esResultRepository;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public int saveBatchESResult(List<ESResult> esResults) {
        if(esResults.size() == 0) throw new BusinessException(ErrorCode.NULL_ERROR);
        for(ESResult esResult : esResults) esResult.setId(UUID.randomUUID().toString());
        try{
            esResultRepository.saveAll(esResults);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,e.getMessage());
        }
        return 1;
    }

    @Override
    public int saveESResult(ESResult esResult) {
        if(esResult == null) throw new BusinessException(ErrorCode.NULL_ERROR);
        esResult.setId(UUID.randomUUID().toString());
        try{
            esResultRepository.save(esResult);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,e.getMessage());
        }

        return 1;
    }


    @Override
    public boolean deleteESResult(String id) {
        try{
            esResultRepository.deleteById(id);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"ID错误");
        }
        return true;
    }

    @Override
    public int updateESResult(ESResult esResult) {
        if(StringUtils.isBlank(esResult.getId())) throw new BusinessException(ErrorCode.NULL_ERROR);
        try {
            esResultRepository.save(esResult);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,e.getMessage());
        }
        return 1;
    }

    @Override
    public Page<ESResult> listESResult(Integer page,Integer size) {
        Page<ESResult> results = esResultRepository.findAll(PageRequest.of(page, size));
        return results;
    }


}
