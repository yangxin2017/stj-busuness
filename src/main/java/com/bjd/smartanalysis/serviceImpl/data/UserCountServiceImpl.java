package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.data.UserCount;
import com.bjd.smartanalysis.mapper.data.UserCountMapper;
import com.bjd.smartanalysis.service.data.UserCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCountServiceImpl extends ServiceImpl<UserCountMapper, UserCount> implements UserCountService {
    @Autowired
    private UserCountMapper mapper;

    @Override
    public UserCount GetByTime(String datestr) {
        QueryWrapper<UserCount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("datestr", datestr);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }
}
