package edu.zuel.hahasearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zuel.hahasearch.model.domain.Tenant;
import edu.zuel.hahasearch.service.TenantService;
import edu.zuel.hahasearch.mapper.TenantMapper;
import org.springframework.stereotype.Service;

/**
* @author SydZh
* @description 针对表【tenant(tenant 租户表)】的数据库操作Service实现
* @createDate 2023-07-06 15:58:13
*/
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant>
    implements TenantService{

}




