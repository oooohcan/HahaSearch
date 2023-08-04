package edu.zuel.hahasearch.controller;

import edu.zuel.hahasearch.common.BaseResponse;
import edu.zuel.hahasearch.common.ResultUtils;
import edu.zuel.hahasearch.dao.ESResultRepository;
import edu.zuel.hahasearch.model.domain.ESResult;
import edu.zuel.hahasearch.model.request.ESAddBatchRequest;
import edu.zuel.hahasearch.model.request.ESAddOneRequest;
import edu.zuel.hahasearch.service.ESResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/es")
@Slf4j
public class ESResultController {
    @Autowired
    private ESResultService esResultService;
    @Autowired
    private ESResultRepository resultRepository;

    @PostMapping("/add-batch")
    public BaseResponse<Integer> addBatchESResult(@RequestBody ESAddBatchRequest esAddBatchRequest){
        List<ESAddOneRequest> esList = esAddBatchRequest.getEsList();
        List<ESResult> esResults = new ArrayList<>();
        for(ESAddOneRequest es : esList){
            ESResult esResult = new ESResult();
            BeanUtils.copyProperties(esResult,es);
            esResults.add(esResult);
        }
        int save = esResultService.saveBatchESResult(esResults);
        return ResultUtils.success(save);
    }

    @PostMapping("/add-one")
    public BaseResponse<Integer> addOneESResult(@RequestBody ESAddOneRequest esAddOneRequest){
        ESResult esResult = new ESResult();
        BeanUtils.copyProperties(esResult, esAddOneRequest);
        int save = esResultService.saveESResult(esResult);
        return ResultUtils.success(save);
    }



}
