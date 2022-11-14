package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.data.TriptypeCount;
import com.bjd.smartanalysis.mapper.data.TriptypeCountMapper;
import com.bjd.smartanalysis.service.data.TriptypeCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TriptypeCountServiceImpl extends ServiceImpl<TriptypeCountMapper, TriptypeCount> implements TriptypeCountService {
    @Autowired
    private TriptypeCountMapper mapper;

    @Override
    public TriptypeCount GetByType(String tripType) {
        QueryWrapper<TriptypeCount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trip_type", tripType);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }
}
