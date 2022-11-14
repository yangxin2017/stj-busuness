package com.bjd.smartanalysis.service.flow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.flow.SysFlowTemplateNode;

import java.util.List;

public interface SysFlowTemplateNodeService extends IService<SysFlowTemplateNode> {
    public List<SysFlowTemplateNode> GetListNodesById(Integer id);

    public SysFlowTemplateNode GetListNodesById(Integer id, Integer step);
}
