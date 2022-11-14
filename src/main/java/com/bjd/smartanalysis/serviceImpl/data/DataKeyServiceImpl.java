package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.data.DataKey;
import com.bjd.smartanalysis.mapper.data.DataKeyMapper;
import com.bjd.smartanalysis.service.data.DataKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataKeyServiceImpl extends ServiceImpl<DataKeyMapper, DataKey> implements DataKeyService {
    @Autowired
    private DataKeyMapper mapper;

    @Override
    public DataKey GetByType(String keyType) {
        QueryWrapper<DataKey> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("key_type", keyType);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }
}
