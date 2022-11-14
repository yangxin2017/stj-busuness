package com.bjd.smartanalysis.controller.flow;

import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.data.DataFhTask;
import com.bjd.smartanalysis.entity.data.DataFhYx;
import com.bjd.smartanalysis.entity.data.DataResult;
import com.bjd.smartanalysis.entity.flow.SysFlow;
import com.bjd.smartanalysis.entity.flow.SysFlowTemplateNode;
import com.bjd.smartanalysis.service.data.DataFhErrService;
import com.bjd.smartanalysis.service.data.DataFhTaskService;
import com.bjd.smartanalysis.service.data.DataFhYxService;
import com.bjd.smartanalysis.service.data.DataResultService;
import com.bjd.smartanalysis.service.flow.SysFlowService;
import com.bjd.smartanalysis.service.flow.SysFlowTemplateNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api(value = "流程管理", tags = {"流程管理"})
@RestController
@RequestMapping("flow")
public class FlowController {
    @Autowired
    private SysFlowTemplateNodeService nodeService;
    @Autowired
    private SysFlowService flowService;
    @Autowired
    private DataFhTaskService taskService;
    @Autowired
    private DataResultService resultService;

    @Autowired
    private DataFhYxService yxService;
    @Autowired
    private DataFhErrService errService;

    @PostMapping("add")
    @ApiOperation(value = "添加流程", notes = "添加流程")
    public ResponseData AddFlow(Integer taskId){
        DataFhTask task = taskService.getById(taskId);
        Integer templateId = 1;
        if(task != null) {
            SysFlow existFlow = flowService.GetFlowByTaskId(taskId);
            if(existFlow != null) {
                flowService.removeById(existFlow.getId());
            }

            List<SysFlowTemplateNode> nodes = nodeService.GetListNodesById(templateId);

            SysFlowTemplateNode curNode = nodes.get(1);
            SysFlow flow = new SysFlow();
            flow.setTaskId(taskId);
            flow.setTemplateId(templateId);
            flow.setStatus(curNode.getName());
            flow.setIsFinish(0);
            flow.setCurNodeStep(curNode.getStep());
            flow.setLastCheckResult("");
            flow.setLastCheckUser(null);
            flow.setTime(new Date());
            flowService.save(flow);
        }
        return ResponseData.OK(null);
    }

    @PostMapping("next")
    @ApiOperation(value = "前进流程", notes = "前进流程")
    public ResponseData FlowNext(Integer flowId, String reason){
        SysFlow flow = flowService.getById(flowId);
        if(flow != null) {
            Integer templateId = 1;
            List<SysFlowTemplateNode> nodes = nodeService.GetListNodesById(templateId);

            SysFlowTemplateNode nextNode = GetNextNode(nodes, flow.getCurNodeStep());
            if(nextNode != null) {
                flow.setTime(new Date());
                flow.setCurNodeStep(nextNode.getStep());
                flow.setStatus(nextNode.getName());
                flow.setLastCheckResult(reason);
                if (nextNode.getName().equals("数据已发放")) {
                    flow.setIsFinish(1);
                    // 保存发放量
                    DataFhTask task = taskService.getById(flow.getTaskId());
                    if(task != null) {
                        String datasource = task.getDataSource();
                        String groupdate = task.getGroupDate();
                        DataResult res = resultService.GetBySourceAndDate(datasource, groupdate);

                        Double bx_ff = yxService.GetCNumber(datasource, groupdate, "walk") + errService.GetJZNumber(datasource, groupdate, "walk");
                        Double qx_ff = yxService.GetCNumber(datasource, groupdate, "cycle") + errService.GetJZNumber(datasource, groupdate, "cycle");
                        Double gj_ff = yxService.GetCNumber(datasource, groupdate, "GJ") + errService.GetJZNumber(datasource, groupdate, "GJ");
                        Double dt_ff = yxService.GetCNumber(datasource, groupdate, "DT") + errService.GetJZNumber(datasource, groupdate, "DT");
                        if (res != null) {
                            res.setBxFf(bx_ff);
                            res.setQxFf(qx_ff);
                            res.setGjFf(gj_ff);
                            res.setDtFf(dt_ff);
                            resultService.updateById(res);
                        }
                    }
                }
                flowService.updateById(flow);
            }
            return ResponseData.OK(null);
        }
        return ResponseData.FAIL("流程不存在");
    }

    @PostMapping("last")
    @ApiOperation(value = "回退流程", notes = "回退流程")
    public ResponseData FlowPrev(Integer flowId, String reason){
        SysFlow flow = flowService.getById(flowId);
        if(flow != null) {
            Integer templateId = 1;
            List<SysFlowTemplateNode> nodes = nodeService.GetListNodesById(templateId);

            SysFlowTemplateNode lastNode = GetLastNode(nodes, flow.getCurNodeStep());
            if(lastNode != null && lastNode.getStep() > 1) {
                flow.setTime(new Date());
                flow.setCurNodeStep(lastNode.getStep());
                flow.setStatus(lastNode.getName());
                flow.setLastCheckResult(reason);
                flowService.updateById(flow);
            }
            return ResponseData.OK(null);
        }
        return ResponseData.FAIL("流程不存在");
    }

    private SysFlowTemplateNode GetNextNode(List<SysFlowTemplateNode> nodes, Integer curStep) {
        SysFlowTemplateNode node = null;
        Integer step = curStep + 1;
        for(SysFlowTemplateNode n: nodes){
            if (n.getStep().equals(step)) {
                node = n;
            }
        }
        return node;
    }
    private SysFlowTemplateNode GetLastNode(List<SysFlowTemplateNode> nodes, Integer curStep) {
        SysFlowTemplateNode node = null;
        Integer step = curStep - 1;
        if (step > 1) {
            for (SysFlowTemplateNode n : nodes) {
                if (n.getStep().equals(step)) {
                    node = n;
                }
            }
        }
        return node;
    }
}
