package com.bjd.smartanalysis.controller.data;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.data.*;
import com.bjd.smartanalysis.entity.flow.SysFlow;
import com.bjd.smartanalysis.entity.flow.SysFlowTemplateNode;
import com.bjd.smartanalysis.service.data.DataFhErrService;
import com.bjd.smartanalysis.service.data.DataFhTaskService;
import com.bjd.smartanalysis.service.data.DataFhYxService;
import com.bjd.smartanalysis.service.flow.SysFlowService;
import com.bjd.smartanalysis.service.flow.SysFlowTemplateNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Api(value = "复核数据", tags = {"复核数据"})
@RestController
@RequestMapping("fh")
public class FhDataController {
    @Autowired
    private DataFhErrService errService;
    @Autowired
    private DataFhYxService yxService;
    @Autowired
    private DataFhTaskService taskService;
    @Autowired
    private SysFlowService flowService;
    @Autowired
    private SysFlowTemplateNodeService nodeService;

    @GetMapping("search")
    @ApiOperation(value = "数据查询", notes = "数据查询")
    private ResponseData SearchYxList(String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        PageData<DataFhYx> datas = yxService.GetPageData(datasource, tripType, stime, etime, pageIndex, pageSize);
        return ResponseData.OK(datas);
    }
    @GetMapping("yxexport")
    @ApiOperation(value = "导出有效数据", notes = "导出有效数据")
    private void ExportErrList(HttpServletResponse response, String datasource, String tripType, String stime, String etime) {
        PageData<DataFhYx> datas = yxService.GetPageData(datasource, tripType, stime, etime, 1, Integer.MAX_VALUE);
        List<DataFhYx> lists = datas.getData();
        try{
            String filename = "数据导出_" + DateUtil.format(new Date(), "YYYY_MM_DD_HH_mm_ss") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            EasyExcel.write(response.getOutputStream(), DataFhYx.class).sheet().doWrite(lists);
        }catch (Exception e){
            System.out.println("导出失败！");
            e.printStackTrace();
        }
    }

    @GetMapping("errlist")
    @ApiOperation(value = "获取错误数据详情列表", notes = "获取错误数据详情列表")
    private ResponseData GetErrList(String datasource, String groupDate, String stime, String etime, String tripType, String errType, Integer pageIndex, Integer pageSize) {
        PageData<DataFhErr> datas = errService.GetDataByErrType(datasource, groupDate, stime, etime, tripType, errType, pageIndex, pageSize);
        return ResponseData.OK(datas);
    }

    @GetMapping("errexport")
    @ApiOperation(value = "导出错误数据", notes = "导出错误数据")
    private void ExportErrList(HttpServletResponse response, String datasource, String groupDate, String stime, String etime, String tripType, String errType) {
        PageData<DataFhErr> datas = errService.GetDataByErrType(datasource, groupDate, stime, etime, tripType, errType, 1, Integer.MAX_VALUE);
        List<DataFhErr> lists = datas.getData();
        try{
            String filename = "数据导出_" + DateUtil.format(new Date(), "YYYY_MM_DD_HH_mm_ss") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            EasyExcel.write(response.getOutputStream(), DataFhErr.class).sheet().doWrite(lists);
        }catch (Exception e){
            System.out.println("导出失败！");
            e.printStackTrace();
        }
    }

    @GetMapping("errTotal")
    @ApiOperation(value = "获取错误统计数据", notes = "获取错误统计数据")
    private ResponseData GetErrList(String datasource, String groupDate) {
        Integer errCount = errService.GetCountByDatasourceAndTime(datasource, groupDate);

        Integer yxCount = yxService.GetCountByDatasourceAndTime(datasource, groupDate);
        Integer total = errCount + yxCount;

        PageData<DataFhYx> ones = yxService.GetPageData(datasource, groupDate, 1, 1);
        List<DataFhYx> datas = ones.getData();
        String datetime = "";
        if (datas != null && datas.size() > 0) {
            DataFhYx d = datas.get(0);
            datetime = d.getGroupDate();
        }

        JSONObject obj = new JSONObject();
        obj.put("error", errCount);
        obj.put("yx", yxCount);
        obj.put("total", total);
        obj.put("time", datetime);
        obj.put("datasource", datasource);
        return ResponseData.OK(obj);
    }

    @GetMapping("yxlist")
    @ApiOperation(value = "获取有效数据详情列表", notes = "获取有效数据详情列表")
    private ResponseData GetYxList(Integer fileId, Integer pageIndex, Integer pageSize) {
        PageData<DataFhYx> datas = yxService.GetPageData(fileId, pageIndex, pageSize);
        return ResponseData.OK(datas);
    }

    @PostMapping("setstatus")
    @ApiOperation(value = "设置错误数据状态", notes = "设置错误数据状态")
    public ResponseData SetDataStatus(String dataId, String status) {
        errService.SetDataStatus(dataId, status);
        return ResponseData.OK(null);
    }

    @GetMapping("list")
    @ApiOperation(value = "领导审核复核列表", notes = "领导审核复核列表")
    private ResponseData GetFhTotalList(String datasource, String groupDate, Integer pageIndex, Integer pageSize) {
        PageData<DataFhTask> page = taskService.GetPageTask(datasource, groupDate, pageIndex, pageSize);
        List<DataFhTask> fhTasks = page.getData();
        JSONArray arr = new JSONArray();
        for(DataFhTask t: fhTasks) {
            JSONObject obj = new JSONObject();
            obj.put("datasource", t.getDataSource());
            obj.put("time", t.getGroupDate());
            obj.put("errCount", t.getErrCount());
            obj.put("totalCount", t.getTotalCount());
            Double yxNum = t.getYxNum();
            Double errNum = t.getErrNum();

            obj.put("yxNumber", yxNum);
            obj.put("yxCount", t.getYxCount());
            obj.put("errNumber", errNum);
            obj.put("status", t.getStatus());

            SysFlow sf = flowService.GetFlowByTaskId(t.getId());
            obj.put("flow", sf);
            if (sf != null) {
                SysFlowTemplateNode node = nodeService.GetListNodesById(sf.getTemplateId(), sf.getCurNodeStep());
                obj.put("flowNode", node);
            }
            arr.add(obj);
        }
        JSONObject o = new JSONObject();
        o.put("total", page.getTotal());
        o.put("data", arr);
        return ResponseData.OK(o);
    }
}
