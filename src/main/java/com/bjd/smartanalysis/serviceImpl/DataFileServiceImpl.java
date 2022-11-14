package com.bjd.smartanalysis.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.DataFile;
import com.bjd.smartanalysis.mapper.DataFileMapper;
import com.bjd.smartanalysis.service.DataFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataFileServiceImpl extends ServiceImpl<DataFileMapper, DataFile> implements DataFileService {
    @Autowired
    private DataFileMapper mapper;

    @Override
    public List<DataFile> GetFileByBid(Integer bid) {
        QueryWrapper<DataFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("belong_id", bid);
        List<DataFile> files = mapper.selectList(queryWrapper);
        return files;
    }
}
