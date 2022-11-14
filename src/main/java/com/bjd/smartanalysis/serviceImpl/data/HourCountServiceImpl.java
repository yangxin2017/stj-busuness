package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.data.HourCount;
import com.bjd.smartanalysis.mapper.data.HourCountMapper;
import com.bjd.smartanalysis.service.data.HourCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HourCountServiceImpl extends ServiceImpl<HourCountMapper, HourCount> implements HourCountService {
    @Autowired
    private HourCountMapper mapper;

    @Override
    public HourCount GetByHour(String hstr) {
        QueryWrapper<HourCount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hstr", hstr);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }
}
