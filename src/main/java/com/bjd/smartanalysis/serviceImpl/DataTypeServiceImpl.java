package com.bjd.smartanalysis.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.DataType;
import com.bjd.smartanalysis.mapper.DataTypeMapper;
import com.bjd.smartanalysis.service.DataTypeService;
import org.springframework.stereotype.Service;

@Service
public class DataTypeServiceImpl extends ServiceImpl<DataTypeMapper, DataType> implements DataTypeService {
}
