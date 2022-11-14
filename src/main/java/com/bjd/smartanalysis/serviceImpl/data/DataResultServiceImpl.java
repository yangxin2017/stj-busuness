package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.data.DataResult;
import com.bjd.smartanalysis.mapper.data.DataResultMapper;
import com.bjd.smartanalysis.service.data.DataResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataResultServiceImpl extends ServiceImpl<DataResultMapper, DataResult> implements DataResultService {
    @Autowired
    private DataResultMapper mapper;

    @Override
    public DataResult GetBySourceAndDate(String dataSource, String groupDate) {
        QueryWrapper<DataResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_source", dataSource);
        queryWrapper.eq("group_date", groupDate);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public List<DataResult> GetList(String dataSource, String stime, String etime) {
        QueryWrapper<DataResult> queryWrapper = new QueryWrapper<>();
        if(dataSource != null && !dataSource.equals("")) {
            queryWrapper.eq("data_source", dataSource);
        }
        if(stime != null && !stime.equals("")) {
            queryWrapper.ge("group_date", stime);
        }
        if(etime != null && !etime.equals("")) {
            queryWrapper.lt("group_date", etime);
        }
        return mapper.selectList(queryWrapper);
    }

    @Override
    public List<DataResult> GetEqualList(String dataSource, String groupDate) {
        QueryWrapper<DataResult> queryWrapper = new QueryWrapper<>();
        if(dataSource != null && !dataSource.equals("")) {
            queryWrapper.eq("data_source", dataSource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        queryWrapper.orderByDesc("group_date");
        return mapper.selectList(queryWrapper);
    }

    @Override
    public DataResult GetNewestOne() {
        QueryWrapper<DataResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("group_date");
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }
}
