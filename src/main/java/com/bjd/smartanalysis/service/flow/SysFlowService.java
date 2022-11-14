package com.bjd.smartanalysis.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.flow.SysFlow;

import java.util.List;

public interface SysFlowService extends IService<SysFlow> {
    public List<SysFlow> GetFinishFlows();

    public void RemoveByTaskId(Integer taskId);

    public SysFlow GetFlowByTaskId(Integer taskId);
}
