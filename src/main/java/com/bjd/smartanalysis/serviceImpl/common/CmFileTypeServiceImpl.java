package com.bjd.smartanalysis.serviceImpl.common;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.common.CmFileType;
import com.bjd.smartanalysis.mapper.common.CmFileTypeMapper;
import com.bjd.smartanalysis.service.common.CmFileTypeService;
import org.springframework.stereotype.Service;

@Service
public class CmFileTypeServiceImpl extends ServiceImpl<CmFileTypeMapper, CmFileType> implements CmFileTypeService {
}
