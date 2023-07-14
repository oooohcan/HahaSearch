package edu.zuel.hahasearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zuel.hahasearch.model.domain.Session;
import edu.zuel.hahasearch.service.SessionService;
import edu.zuel.hahasearch.mapper.SessionMapper;
import org.springframework.stereotype.Service;

/**
* @author oooohcan
* @description 针对表【session(session 登录状态表)】的数据库操作Service实现
* @createDate 2023-07-06 15:53:45
*/
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session>
    implements SessionService{

}




