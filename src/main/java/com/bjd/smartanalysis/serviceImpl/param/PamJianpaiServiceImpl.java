package com.bjd.smartanalysis.serviceImpl.param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.param.PamJianpai;
import com.bjd.smartanalysis.mapper.param.PamJianpaiMapper;
import com.bjd.smartanalysis.service.param.PamJianpaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PamJianpaiServiceImpl extends ServiceImpl<PamJianpaiMapper, PamJianpai> implements PamJianpaiService {
    @Autowired
    private PamJianpaiMapper mapper;

    @Override
    public List<PamJianpai> GetByType(String type) {
        QueryWrapper<PamJianpai> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        return mapper.selectList(queryWrapper);
    }
}
