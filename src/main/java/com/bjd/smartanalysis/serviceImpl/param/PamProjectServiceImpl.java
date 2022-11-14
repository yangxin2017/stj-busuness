package com.bjd.smartanalysis.serviceImpl.param;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.param.PamProject;
import com.bjd.smartanalysis.mapper.param.PamProjectMapper;
import com.bjd.smartanalysis.service.param.PamProjectService;
import org.springframework.stereotype.Service;

@Service
public class PamProjectServiceImpl extends ServiceImpl<PamProjectMapper, PamProject> implements PamProjectService {
}
