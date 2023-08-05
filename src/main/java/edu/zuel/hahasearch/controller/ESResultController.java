package edu.zuel.hahasearch.controller;
import java.util.Date;

import edu.zuel.hahasearch.common.BaseResponse;
import edu.zuel.hahasearch.common.ErrorCode;
import edu.zuel.hahasearch.common.ResultUtils;
import edu.zuel.hahasearch.dao.ESResultRepository;
import edu.zuel.hahasearch.exception.BusinessException;
import edu.zuel.hahasearch.model.domain.ESResult;
import edu.zuel.hahasearch.model.request.ESAddBatchRequest;
import edu.zuel.hahasearch.model.request.ESAddOneRequest;
import edu.zuel.hahasearch.service.ESResultService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/es")
@Slf4j
public class ESResultController {
    @Autowired
    private ESResultService esResultService;
    @Autowired
    private ESResultRepository esResultRepository;

    @PostMapping("/add-batch")
    public BaseResponse<Integer> addBatchESResult(@RequestBody ESAddBatchRequest esAddBatchRequest){
        // 1、取值添加至list
        List<ESAddOneRequest> esList = esAddBatchRequest.getEsList();
        List<ESResult> esResults = new ArrayList<>();
        for(ESAddOneRequest es : esList){
            ESResult esResult = new ESResult();
            BeanUtils.copyProperties(es,esResult);
            esResults.add(esResult);
        }
        // 2、保存至es
        int save = esResultService.saveBatchESResult(esResults);
        return ResultUtils.success(save);
    }

    @PostMapping("/add-one")
    public BaseResponse<Integer> addOneESResult(@RequestBody ESAddOneRequest esAddOneRequest){
        ESResult esResult = new ESResult();
        BeanUtils.copyProperties(esAddOneRequest,esResult);
        int save = esResultService.saveESResult(esResult);
        return ResultUtils.success(save);
    }

    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteESResult(String id, HttpServletRequest request){
        if(StringUtils.isBlank(id)) throw new BusinessException(ErrorCode.NULL_ERROR);
        boolean result = esResultService.deleteESResult(id);
        return ResultUtils.success(result);
    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateESResult(@RequestBody ESResult esResult, HttpServletRequest request){
        // 1、取值校验
        String id = esResult.getId();
        String title = esResult.getTitle();
        String content = esResult.getContent();
        String website = esResult.getWebsite();
        String imgUrl = esResult.getImgUrl();
        String type = esResult.getType();
        String tenantCode = esResult.getTenantCode();
        Date date = esResult.getDate();
        if(StringUtils.isBlank(id)) throw new BusinessException(ErrorCode.NULL_ERROR,"ID不能为空");
        if(StringUtils.isAnyBlank(title,content,website,imgUrl,type,tenantCode)) throw new BusinessException(ErrorCode.NULL_ERROR,"请求参数不能存在空值");
        if(date == null) throw new BusinessException(ErrorCode.NULL_ERROR,"日期不能为空");
        // 2、保存至es
        int result = esResultService.updateESResult(esResult);
        return ResultUtils.success(result);
    }

    @GetMapping("/find")
    public BaseResponse<ESResult> findById(String id, HttpServletRequest request){
        ESResult esResult = esResultRepository.findESResultById(id);
        return ResultUtils.success(esResult);
    }

    @GetMapping("search")
    public BaseResponse<Page<ESResult>> searchByTitleOrContent(String keyword, Integer page, Integer size, HttpServletRequest request){
        Page<ESResult> esResults = esResultRepository.searchESResultByTitleOrContent(keyword, keyword, PageRequest.of(page-1, size));
        return ResultUtils.success(esResults);
    }

    @GetMapping("search-website")
    public BaseResponse<Page<ESResult>> searchByWebsite(String keyword,Integer page,Integer size, HttpServletRequest request){
        Page<ESResult> esResults = esResultRepository.searchESResultByWebsiteLike(keyword, PageRequest.of(page-1, size));
        return ResultUtils.success(esResults);
    }

    @GetMapping("search-type")
    public BaseResponse<Page<ESResult>> searchByType(String keyword,Integer page,Integer size, HttpServletRequest request){
        Page<ESResult> esResults = esResultRepository.searchESResultByTypeLike(keyword, PageRequest.of(page-1, size));
        return ResultUtils.success(esResults);
    }

}
