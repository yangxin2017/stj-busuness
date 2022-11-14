package com.bjd.smartanalysis.serviceImpl.flow;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.flow.SysFlowTemplateNode;
import com.bjd.smartanalysis.mapper.flow.SysFlowTemplateNodeMapper;
import com.bjd.smartanalysis.service.flow.SysFlowTemplateNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFlowTemplateNodeServiceImpl extends ServiceImpl<SysFlowTemplateNodeMapper, SysFlowTemplateNode> implements SysFlowTemplateNodeService {
    @Autowired
    private SysFlowTemplateNodeMapper mapper;

    @Override
    public List<SysFlowTemplateNode> GetListNodesById(Integer id) {
        QueryWrapper<SysFlowTemplateNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", id);
        queryWrapper.orderByAsc("step");
        return mapper.selectList(queryWrapper);
    }

    @Override
    public SysFlowTemplateNode GetListNodesById(Integer id, Integer step) {
        QueryWrapper<SysFlowTemplateNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", id);
        queryWrapper.eq("step", step);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }
}
