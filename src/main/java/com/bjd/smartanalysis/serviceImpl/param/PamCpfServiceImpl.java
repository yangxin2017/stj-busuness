package com.bjd.smartanalysis.serviceImpl.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.param.PamCpf;
import com.bjd.smartanalysis.mapper.param.PamCpfMapper;
import com.bjd.smartanalysis.service.param.PamCpfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PamCpfServiceImpl extends ServiceImpl<PamCpfMapper, PamCpf> implements PamCpfService {
    @Autowired
    private PamCpfMapper mapper;

    @Override
    public PamCpf GetOneByTime(String time) {
        QueryWrapper<PamCpf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("time", time);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }
}
