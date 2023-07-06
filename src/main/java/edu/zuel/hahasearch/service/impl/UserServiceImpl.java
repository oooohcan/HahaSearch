package edu.zuel.hahasearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zuel.hahasearch.model.domain.User;
import edu.zuel.hahasearch.service.UserService;
import edu.zuel.hahasearch.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author SydZh
* @description 针对表【user(user 用户表)】的数据库操作Service实现
* @createDate 2023-07-06 15:58:26
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




