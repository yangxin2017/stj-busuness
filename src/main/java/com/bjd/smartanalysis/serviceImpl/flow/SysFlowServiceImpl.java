package com.bjd.smartanalysis.serviceImpl.flow;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.flow.SysFlow;
import com.bjd.smartanalysis.mapper.flow.SysFlowMapper;
import com.bjd.smartanalysis.service.flow.SysFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFlowServiceImpl extends ServiceImpl<SysFlowMapper, SysFlow> implements SysFlowService {
    @Autowired
    private SysFlowMapper mapper;

    @Override
    public List<SysFlow> GetFinishFlows() {
        QueryWrapper<SysFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_finish", 1);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public void RemoveByTaskId(Integer taskId) {
        QueryWrapper<SysFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        mapper.delete(queryWrapper);
    }

    @Override
    public SysFlow GetFlowByTaskId(Integer taskId) {
        QueryWrapper<SysFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }
}
