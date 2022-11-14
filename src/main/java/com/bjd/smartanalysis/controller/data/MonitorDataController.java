package com.bjd.smartanalysis.controller.data;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.common.CmFile;
import com.bjd.smartanalysis.entity.data.DataCheckErr;
import com.bjd.smartanalysis.entity.data.DataCheckYx;
import com.bjd.smartanalysis.entity.data.DataMonitorErr;
import com.bjd.smartanalysis.entity.data.DataMonitorYx;
import com.bjd.smartanalysis.service.common.CmFileService;
import com.bjd.smartanalysis.service.data.DataMonitorDetailService;
import com.bjd.smartanalysis.service.data.DataMonitorErrService;
import com.bjd.smartanalysis.service.data.DataMonitorYxService;
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

@Api(value = "监测数据", tags = {"监测数据"})
@RestController
@RequestMapping("monitor")
public class MonitorDataController {
    @Autowired
    private CmFileService fileService;
    @Autowired
    private DataMonitorYxService monitorYxService;
    @Autowired
    private DataMonitorErrService monitorErrService;

    private Integer typeId = 1;

    @GetMapping("search")
    @ApiOperation(value = "数据查询", notes = "数据查询")
    public ResponseData SearchList(String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        PageData<DataMonitorYx> page = monitorYxService.GetListByFileId(datasource, tripType, stime, etime, null, pageIndex, pageSize);
        return ResponseData.OK(page);
    }

    @GetMapping("yxexport")
    @ApiOperation(value = "导出有效数据", notes = "导出有效数据")
    private void ExportYxList(HttpServletResponse response, String datasource, String tripType, String stime, String etime) {
        PageData<DataMonitorYx> page = monitorYxService.GetListByFileId(datasource, tripType, stime, etime, null, 1, Integer.MAX_VALUE);
        List<DataMonitorYx> lists = page.getData();
        try{
            String filename = "数据导出_" + DateUtil.format(new Date(), "YYYY_MM_DD_HH_mm_ss") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            EasyExcel.write(response.getOutputStream(), DataMonitorYx.class).sheet().doWrite(lists);
        }catch (Exception e){
            System.out.println("导出失败！");
            e.printStackTrace();
        }
    }
    @GetMapping("errexport")
    @ApiOperation(value = "导出错误数据", notes = "导出错误数据")
    private void ExportErrList(HttpServletResponse response, Integer fileId, String datasource, String tripType, String stime, String etime) {
        PageData<DataMonitorErr> page = monitorErrService.GetAllData(fileId, datasource, tripType, stime, etime, 1, Integer.MAX_VALUE);
        List<DataMonitorErr> lists = page.getData();
        try{
            String filename = "数据导出_" + DateUtil.format(new Date(), "YYYY_MM_DD_HH_mm_ss") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            EasyExcel.write(response.getOutputStream(), DataMonitorErr.class).sheet().doWrite(lists);
        }catch (Exception e){
            System.out.println("导出失败！");
            e.printStackTrace();
        }
    }

    @GetMapping("list")
    @ApiOperation(value = "获取上传文件列表", notes = "获取上传文件列表")
    public ResponseData GetList(Integer pageIndex, Integer pageSize, String datasource) {
        PageData<CmFile> page = fileService.SelectPage(typeId, datasource, pageIndex, pageSize);
        JSONArray res = new JSONArray();
        List<CmFile> files = page.getData();
        Date datetime = null;

        for(CmFile f: files) {
            datasource = "";
            JSONObject obj = new JSONObject();
            Integer yxCount = monitorYxService.GetCountByFileId(f.getId());
            Integer errCount = monitorErrService.GetCountByFileId(f.getId());

            PageData<DataMonitorYx> ones = monitorYxService.GetListByFileId(f.getId(), 1, 1);
            List<DataMonitorYx> arrYx = ones.getData();
            if(arrYx != null && arrYx.size() > 0) {
                DataMonitorYx od = arrYx.get(0);
                datetime = od.getStime();
                datasource = od.getDataSource();
            } else {
                PageData<DataMonitorErr> errs = monitorErrService.GetAllData(f.getId(), null, null, null, null, 1, 1);
                List<DataMonitorErr> arrErr = errs.getData();
                if (arrErr != null && arrErr.size() > 0) {
                    DataMonitorErr err = arrErr.get(0);
                    datetime = err.getStime();
                    datasource = err.getDataSource();
                }
            }

            obj.put("yxCount", yxCount);
            obj.put("datasource", datasource);
            obj.put("errCount", errCount);
            obj.put("totalCount", yxCount + errCount);
            obj.put("time", f.getCtime());
            obj.put("datatime", datetime);
            obj.put("user", "--");
            obj.put("filename", f.getFileName());
            obj.put("fileId", f.getId());
            obj.put("dataStatus", f.getDataStatus());
            res.add(obj);
        }
        JSONObject d = new JSONObject();
        d.put("total", page.getTotal());
        d.put("data", res);
        return ResponseData.OK(d);
    }

    @GetMapping("errlist")
    @ApiOperation(value = "获取错误数据详情列表", notes = "获取错误数据详情列表")
    public ResponseData GetErrListByType(Integer fileId, String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        PageData<DataMonitorErr> page = monitorErrService.GetAllData(fileId, datasource, tripType, stime, etime, pageIndex, pageSize);
        return ResponseData.OK(page);
    }

    @GetMapping("yxlist")
    @ApiOperation(value = "获取有效数据详情列表", notes = "获取有效数据详情列表")
    public ResponseData GetYxListByType(Integer fileId, Integer pageIndex, Integer pageSize) {
        PageData<DataMonitorYx> page = monitorYxService.GetListByFileId(fileId, pageIndex, pageSize);
        return ResponseData.OK(page);
    }

    @PostMapping("setstatus")
    @ApiOperation(value = "设置错误数据状态", notes = "设置错误数据状态")
    public ResponseData SetDataStatus(String dataId, String status) {
        monitorErrService.SetDataStatus(dataId, status);
        return ResponseData.OK(null);
    }

    @GetMapping("errTotal")
    @ApiOperation(value = "获取错误统计数据", notes = "获取错误统计数据")
    private ResponseData GetErrList(Integer fileId) {
        Integer errCount = monitorErrService.GetCountByFileId(fileId, "ERROR");
        Integer nullCount = monitorErrService.GetCountByFileId(fileId, "NULL");
        Integer repeatCount = monitorErrService.GetCountByFileId(fileId, "REPEAT");

        Integer yxCount = monitorYxService.GetCountByFileId(fileId);
        Integer total = errCount + nullCount + repeatCount + yxCount;

        PageData<DataMonitorYx> ones = monitorYxService.GetListByFileId(fileId, 1, 1);
        List<DataMonitorYx> datas = ones.getData();
        String datetime = "";
        String datasource = "";
        if (datas != null && datas.size() > 0) {
            DataMonitorYx d = datas.get(0);
            datetime = d.getGroupDate();
            datasource = d.getDataSource();
        } else {
            PageData<DataMonitorErr> errs = monitorErrService.GetAllData(fileId, null, null, null, null, 1, 1);
            List<DataMonitorErr> arrErr = errs.getData();
            if (arrErr != null && arrErr.size() > 0) {
                DataMonitorErr err = arrErr.get(0);
                datetime = err.getGroupDate();
                datasource = err.getDataSource();
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("error", errCount);
        obj.put("null", nullCount);
        obj.put("repeat", repeatCount);
        obj.put("yx", yxCount);
        obj.put("total", total);
        obj.put("time", datetime);
        obj.put("datasource", datasource);
        return ResponseData.OK(obj);
    }

}
