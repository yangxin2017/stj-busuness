package com.bjd.smartanalysis.controller.data;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.common.CmFile;
import com.bjd.smartanalysis.entity.data.*;
import com.bjd.smartanalysis.entity.flow.SysFlow;
import com.bjd.smartanalysis.entity.param.PamCpf;
import com.bjd.smartanalysis.entity.param.PamJianpai;
import com.bjd.smartanalysis.entity.param.PamProject;
import com.bjd.smartanalysis.entity.param.PamTransfer;
import com.bjd.smartanalysis.service.common.CmFileService;
import com.bjd.smartanalysis.service.data.*;
import com.bjd.smartanalysis.service.flow.SysFlowService;
import com.bjd.smartanalysis.service.param.PamCpfService;
import com.bjd.smartanalysis.service.param.PamJianpaiService;
import com.bjd.smartanalysis.service.param.PamProjectService;
import com.bjd.smartanalysis.service.param.PamTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(value = "复核数据--核证", tags = {"复核数据--核证"})
@RestController
@RequestMapping("check")
public class CheckDataController {
    @Autowired
    private CmFileService fileService;
    @Autowired
    private DataCheckYxService checkYxService;
    @Autowired
    private DataCheckErrService checkErrService;

    @Autowired
    private DataFhTaskService fhTaskService;
    @Autowired
    private DataFhYxService fhYxService;
    @Autowired
    private DataFhErrService fhErrService;

    @Autowired
    private PamCpfService cpfService;
    @Autowired
    private PamTransferService transferService;
    @Autowired
    private PamProjectService projectService;
    @Autowired
    private PamJianpaiService jianpaiService;

    @Autowired
    private SysFlowService flowService;

    @Autowired
    private DataResultService resultService;

    private Integer typeId = 2;

    @GetMapping("search")
    @ApiOperation(value = "数据查询", notes = "数据查询")
    private ResponseData SearchYxList(String tripType, String datasource, String stime, String etime, Integer pageIndex, Integer pageSize) {
        PageData<DataCheckYx> datas = checkYxService.GetListByFileId(datasource, tripType, stime, etime, pageIndex, pageSize);
        return ResponseData.OK(datas);
    }

    @GetMapping("list")
    @ApiOperation(value = "获取上传文件列表", notes = "获取上传文件列表")
    public ResponseData GetList(Integer pageIndex, Integer pageSize, String datasource) {
        PageData<CmFile> page = fileService.SelectPage(typeId, datasource, pageIndex, pageSize);
        JSONArray res = new JSONArray();
        List<CmFile> files = page.getData();
//        String datetime = null;
        for(CmFile f: files) {
            JSONObject obj = new JSONObject();
            //Integer yxCount = checkYxService.GetCountByFileId(f.getId());
            //Integer errCount = checkErrService.GetCountByFileId(f.getId());

            //PageData<DataCheckYx> ones = checkYxService.GetListByFileId(f.getId(), 1, 1);
            //List<DataCheckYx> datas = ones.getData();
            //if(datas != null && datas.size() > 0) {
            //DataCheckYx d = datas.get(0);
            //datetime = DateUtil.formatDate(d.getStime());

            obj.put("yxCount", f.getYxCount());
            obj.put("errCount", f.getErrCount());
            obj.put("totalCount", f.getTotalCount());
            obj.put("time", f.getCtime());
            obj.put("datatime", f.getDataTime());
            obj.put("datasource", f.getDataSource());
            obj.put("user", "--");
            obj.put("filename", f.getFileName());
            obj.put("fileId", f.getId());
            obj.put("dataStatus", f.getDataStatus());
            res.add(obj);
//            } else {
//                obj.put("yxCount", yxCount);
//                obj.put("errCount", errCount);
//                obj.put("totalCount", yxCount + errCount);
//                obj.put("time", f.getCtime());
//                obj.put("datatime", "--");
//                obj.put("datasource", f.getDataSource());
//                obj.put("dataStatus", f.getDataStatus());
//                obj.put("user", "--");
//                obj.put("filename", f.getFileName());
//                obj.put("fileId", f.getId());
//                res.add(obj);
//            }
        }

        JSONObject o = new JSONObject();
        o.put("total", page.getTotal());
        o.put("data", res);
        return ResponseData.OK(o);
    }

    @GetMapping("errlist")
    @ApiOperation(value = "获取错误数据详情列表", notes = "获取错误数据详情列表")
    private ResponseData GetErrList(Integer fileId, String errType, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        PageData<DataCheckErr> datas = checkErrService.GetDataByErrType(fileId, errType, tripType, stime, etime, pageIndex, pageSize);
        return ResponseData.OK(datas);
    }

    @GetMapping("errexport")
    @ApiOperation(value = "导出错误数据", notes = "导出错误数据")
    private void ExportErrList(HttpServletResponse response, Integer fileId, String errType, String tripType, String stime, String etime) {
        PageData<DataCheckErr> datas = checkErrService.GetDataByErrType(fileId, errType, tripType, stime, etime, 1, Integer.MAX_VALUE);
        List<DataCheckErr> lists = datas.getData();
        try{
            String filename = "数据导出_" + DateUtil.format(new Date(), "YYYY_MM_DD_HH_mm_ss") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            EasyExcel.write(response.getOutputStream(), DataCheckErr.class).sheet().doWrite(lists);
        }catch (Exception e){
            System.out.println("导出失败！");
            e.printStackTrace();
        }
    }

    @GetMapping("yxexport")
    @ApiOperation(value = "导出有效数据", notes = "导出有效数据")
    private void ExportErrList(HttpServletResponse response, String tripType, String datasource, String stime, String etime) {
        PageData<DataCheckYx> datas = checkYxService.GetListByFileId(datasource, tripType, stime, etime, 1, Integer.MAX_VALUE);
        List<DataCheckYx> lists = datas.getData();
        try{
            String filename = "数据导出_" + DateUtil.format(new Date(), "YYYY_MM_DD_HH_mm_ss") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            EasyExcel.write(response.getOutputStream(), DataCheckYx.class).sheet().doWrite(lists);
        }catch (Exception e){
            System.out.println("导出失败！");
            e.printStackTrace();
        }
    }

    @GetMapping("errTotal")
    @ApiOperation(value = "获取错误统计数据", notes = "获取错误统计数据")
    private ResponseData GetErrList(Integer fileId) {
        Integer errCount = checkErrService.GetCountByFileId(fileId, "ERROR");
        Integer nullCount = checkErrService.GetCountByFileId(fileId, "NULL");
        Integer repeatCount = checkErrService.GetCountByFileId(fileId, "REPEAT");

        Integer yxCount = checkYxService.GetCountByFileId(fileId);
        Integer total = errCount + nullCount + repeatCount + yxCount;

        PageData<DataCheckYx> ones = checkYxService.GetListByFileId(fileId, 1, 1);
        List<DataCheckYx> datas = ones.getData();
        String datetime = "";
        String datasource = "";
        if (datas != null && datas.size() > 0) {
            DataCheckYx d = datas.get(0);
            datetime = d.getGroupDate();
            datasource = d.getDataSource();
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

    @GetMapping("yxlist")
    @ApiOperation(value = "获取有效数据详情列表", notes = "获取有效数据详情列表")
    private ResponseData GetYxList(Integer fileId, Integer pageIndex, Integer pageSize) {
        PageData<DataCheckYx> datas = checkYxService.GetListByFileId(fileId, pageIndex, pageSize);
        return ResponseData.OK(datas);
    }

    @PostMapping("setstatus")
    @ApiOperation(value = "设置错误数据状态", notes = "设置错误数据状态")
    public ResponseData SetDataStatus(String dataId, String status) {
        checkErrService.SetDataStatus(dataId, status);
        return ResponseData.OK(null);
    }

    @GetMapping("fhlist")
    @ApiOperation(value = "获取复核数据列表", notes = "获取复核数据列表")
    private ResponseData GetCheckDatas(String datasource, String groupDate, Integer pageIndex, Integer pageSize) {
        JSONArray result = new JSONArray();
        PageData<DataFhTask> pdatas = fhTaskService.GetPageTask(datasource, groupDate, pageIndex, pageSize);
        for(DataFhTask task: pdatas.getData()) {
            JSONObject obj = new JSONObject();
            obj.put("datasource", task.getDataSource());
            obj.put("groupDate", task.getGroupDate());
            obj.put("status", task.getStatus());
            obj.put("fhTime", task.getFhTime());
            obj.put("yxCount", task.getYxCount());
            obj.put("errCount", task.getErrCount());
            obj.put("totalCount", task.getTotalCount());
            SysFlow flow = flowService.GetFlowByTaskId(task.getId());
            if (flow != null) {
                obj.put("flow", flow);
            } else {
                obj.put("flow", null);
            }
            result.add(obj);
        }
//        PageData<String> pdatas = checkYxService.GetGroupCheckData(datasource, groupDate, pageIndex, pageSize);
//        List<String> dates = pdatas.getData();
//        for(String dstrall: dates) {
//            String[] arrstr = dstrall.split("#");
//            if (arrstr.length > 1) {
//                String dstr = arrstr[0];
//                String datasourcestr = arrstr[1];
//                JSONObject obj = new JSONObject();
//                obj.put("datasource", datasourcestr);
//                obj.put("groupDate", dstr);
//                DataFhTask fh = fhTaskService.GetTaskBySourceAndDate(datasourcestr, dstr);
//                if (fh != null) {
//                    obj.put("status", fh.getStatus());
//                    obj.put("fhTime", fh.getFhTime());
//                    obj.put("yxCount", fh.getYxCount());
//                    obj.put("errCount", fh.getErrCount());
//                    obj.put("totalCount", fh.getTotalCount());
//                    SysFlow flow = flowService.GetFlowByTaskId(fh.getId());
//                    if (flow != null) {
//                        obj.put("flow", flow);
//                    } else {
//                        obj.put("flow", null);
//                    }
//                } else {
//                    obj.put("status", "未复核");
//                    obj.put("fhTime", null);
//                    obj.put("yxCount", 0);
//                    obj.put("errCount", 0);
//                    Integer total = checkYxService.GetCountBySourceAndDate(datasourcestr, dstr);
//                    obj.put("totalCount", total);
//                }
//                result.add(obj);
//            }
//        }
        JSONObject o = new JSONObject();
        o.put("total", pdatas.getTotal());
        o.put("data", result);
        return ResponseData.OK(o);
    }

    @PostMapping("fh")
    @ApiOperation(value = "复核数据", notes = "复核数据")
    private ResponseData FhData(String datasource, String dstr) {
        Integer total = checkYxService.GetCountBySourceAndDate(datasource, dstr);
        System.out.println(total);

        // 获取参数
        List<PamCpf> p1s = cpfService.list();
        List<PamTransfer> p2s = transferService.list();
        List<PamProject> p3s = projectService.list();
        List<PamJianpai> p4s = jianpaiService.list();
        boolean isOK = true;


        System.out.println("start");
        // 删除之前复核的数据
        fhYxService.DeleteByGroupDate(datasource, dstr);
        fhErrService.DeleteByGroupDate(datasource, dstr);


        DataFhTask existTask = fhTaskService.GetTaskBySourceAndDate(datasource, dstr);
        if (existTask != null) {
            flowService.RemoveByTaskId(existTask.getId());
            fhTaskService.removeById(existTask.getId());
        }
        ////
        if (p1s.size() > 0 && p2s.size() > 0 && p3s.size() > 0 && p4s.size() > 0) {
            int count = 10000;
            Integer pages = total / count + 1;
            System.out.println("pages====" + pages);

            DataFhTask et = null;

            for(int i=1;i<=pages;i++) {

                List<DataFhYx> fhYxes = new ArrayList<>();
                List<DataFhErr> fhErrs = new ArrayList<>();

                System.out.println("deal ont" + i + "----counbt:::" + pages);
                PageData<DataCheckYx> tmp = checkYxService.GetGroupCheckDataList(datasource, dstr, i,  count);
                List<DataCheckYx> ready = tmp.getData();

                try {
                    PamTransfer transfer = p2s.get(0);
                    PamProject project = p3s.get(0);
                    for (DataCheckYx yx : ready) {
                        Date stime = yx.getStime();
                        String tripType = yx.getTripType();
                        String tripLengthstr = yx.getTripCheckLength();
                        String tripRealLengthstr = yx.getTripLength();
                        Double checkLength = Double.parseDouble(tripLengthstr);

                        if (checkLength <= 0) {
                            checkLength = Double.parseDouble(tripRealLengthstr);
                        }

//                        if(tripType.equals("DT") || tripType.equals("GJ")) {
//                            checkLength = Double.parseDouble(tripRealLengthstr);
//                        }

                        Double cSend = yx.getCSend();
                        String stimeStr = DateUtil.format(stime, "M/d");
                        stimeStr = "2021/" + stimeStr;
                        PamCpf cpf = this.GetOneByTime(stimeStr, p1s);
                        if (cpf == null) {
                            isOK = false;
                            break;
                        }
                        String hour = DateUtil.format(stime, "H");
                        Integer h = Integer.parseInt(hour) + 1;
                        String fieldName = "p" + hour + "_" + h;
                        if (h == 25) {
                            fieldName = "p23_24";
                        }
                        Field[] fs = cpf.getClass().getDeclaredFields();
                        Field field = null;
                        for (Field f : fs) {
                            if (f.getName().equals(fieldName)) {
                                field = f;
                                break;
                            }
                        }
                        if (field != null) {
                            field.setAccessible(true);
                            Object vo = field.get(cpf);
                            Double val = Double.parseDouble(vo.toString());
                            checkLength = checkLength / 1000;
                            Double calcRightVal = val * (GetValTransfer(tripType, transfer) * checkLength) - GetValProject(tripType, project) * checkLength;

                            // 计算左边公式
                            PamJianpai pamJP = GetJPByTime(tripType, stimeStr, fieldName, p4s);
                            if (pamJP != null) {
                                Double jpVal = GetObjectByFieldName(pamJP, fieldName);
                                Double calcLeftValue = jpVal * checkLength;

                                System.out.println("val====" + val + ",tripType:::" + tripType + ",tripTypeVal:::" +
                                        GetValTransfer(tripType, transfer) + ",checkLength:::" + checkLength +
                                        ",projectVal:::" + GetValProject(tripType, project) +
                                        ",fieldName:::" + stimeStr + " " +fieldName +
                                        ",calcRightVal:::" + calcRightVal + ",jpVal:::" + jpVal + ",calcLeftValue:::" + calcLeftValue);

                                // 判断数据是否有问题？
                                if (Math.abs((calcRightVal - cSend)/calcRightVal )> 0.01) {
                                    DataFhErr fe = GetDataFhErr(yx, calcLeftValue, calcRightVal);
                                    fhErrs.add(fe);
                                } else {
                                    DataFhYx fy = GetDataFhYx(yx, calcLeftValue, calcRightVal);
                                    fhYxes.add(fy);
                                }
                            }
                        }
                    }
                    if (!isOK) {
                        return ResponseData.FAIL("汽车系统日期不存在");
                    } else {
                        fhYxService.InsertBatch(fhYxes);
                        fhErrService.saveBatch(fhErrs, 500);

                        Double yxNum = 0.0;
                        for (DataFhYx d : fhYxes) {
                            double cpf = d.getCSendRight();
                            yxNum += cpf;
                        }
                        Double errNum = 0.0;
                        for (DataFhErr d : fhErrs) {
                            double cpf = d.getCSendRight();
                            if (d.getDataStatus().equals("忽略")) {
                                yxNum += cpf;
                            } else {
                                errNum += cpf;
                            }
                        }

                        et = fhTaskService.GetTaskBySourceAndDate(datasource, dstr);
                        if (et == null) {
                            DataFhTask task = new DataFhTask();
                            task.setDataSource(datasource);
                            task.setGroupDate(dstr);
                            task.setStatus("");
                            task.setFhTime(new Date());
                            task.setYxCount(fhYxes.size());
                            task.setErrCount(fhErrs.size());
                            task.setTotalCount(fhYxes.size() + fhErrs.size());
                            task.setYxNum(yxNum);
                            task.setErrNum(errNum);
                            task.setTotalNum(yxNum + errNum);
                            fhTaskService.save(task);
                        } else {
                            Integer n1 = et.getYxCount();
                            Integer n2 = et.getErrCount();
                            Integer n3 = et.getTotalCount();
                            Double n11 = et.getYxNum();
                            Double n12 = et.getErrNum();
                            Double n13 = et.getTotalNum();

                            et.setYxCount(fhYxes.size() + n1);
                            et.setErrCount(fhErrs.size() + n2);
                            et.setTotalCount(fhYxes.size() + fhErrs.size() + n3);
                            et.setYxNum(yxNum + n11);
                            et.setErrNum(errNum + n12);
                            et.setTotalNum(yxNum + errNum + n13);
                            fhTaskService.updateById(et);
                        }

                        // 保存申报量数据
                        double bx_sb = 0.0;
                        double qx_sb = 0.0;
                        double gj_sb = 0.0;
                        double dt_sb = 0.0;
                        for (DataFhYx d : fhYxes) {
                            double cpf = d.getCSendRight();
                            String type = d.getTripType();
                            if (type.equals("walk")) {
                                bx_sb += cpf;
                            } else if (type.equals("cycle") || type.equals("ecycle")) {
                                qx_sb += cpf;
                            } else if (type.equals("GJ")) {
                                gj_sb += cpf;
                            } else if (type.equals("DT")) {
                                dt_sb += cpf;
                            }
                        }
                        for (DataFhErr d : fhErrs) {
                            double cpf = d.getCSendRight();
                            String type = d.getTripType();
                            if (type.equals("walk")) {
                                bx_sb += cpf;
                            } else if (type.equals("cycle") || type.equals("ecycle")) {
                                qx_sb += cpf;
                            } else if (type.equals("GJ")) {
                                gj_sb += cpf;
                            } else if (type.equals("DT")) {
                                dt_sb += cpf;
                            }
                        }

                        DataResult res = resultService.GetBySourceAndDate(datasource, dstr);
                        if (res == null) {
                            res = new DataResult();
                            res.setDataSource(datasource);
                            res.setGroupDate(dstr);
                            res.setBxSb(bx_sb);
                            res.setQxSb(qx_sb);
                            res.setGjSb(gj_sb);
                            res.setDtSb(dt_sb);
                            res.setBxFf(0.0);
                            res.setQxFf(0.0);
                            res.setGjFf(0.0);
                            res.setDtFf(0.0);
                            resultService.save(res);
                        } else {
                            double n1 = res.getBxSb();
                            double n2 = res.getQxSb();
                            double n3 = res.getGjSb();
                            double n4 = res.getDtSb();
                            res.setBxSb(bx_sb + n1);
                            res.setQxSb(qx_sb + n2);
                            res.setGjSb(gj_sb + n3);
                            res.setDtSb(dt_sb + n4);
                            res.setBxFf(0.0);
                            res.setQxFf(0.0);
                            res.setGjFf(0.0);
                            res.setDtFf(0.0);
                            resultService.updateById(res);
                        }

                        // 前端发起 保存流程
                        ////////////////return ResponseData.OK(et);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ResponseData.FAIL("计算出错！");
                }
            }

            return ResponseData.OK(et);
        } else {
            return ResponseData.FAIL("系数不存在");
        }
    }

    private Float GetValTransfer(String type, PamTransfer transfer) {
        Float v = 0.0f;
        if (type.equals("DT")) {
            v = transfer.getDt();
        }else if(type.equals("walk")){
            v = transfer.getCx();
        }else if(type.equals("ecycle") || type.equals("cycle")){
            v = transfer.getZxc();
        }else if(type.equals("GJ")){
            v = transfer.getGj();
        }
        return v;
    }
    private Float GetValProject(String type, PamProject project) {
        Float v = 0.0f;
        if (type.equals("DT")) {
            v = project.getDt();
        }else if(type.equals("walk")){
            v = project.getBx();
        }else if(type.equals("ecycle") || type.equals("cycle")){
            v = project.getZxc();
        }else if(type.equals("GJ")){
            v = project.getGj();
        }
        return v;
    }

    private PamCpf GetOneByTime(String time, List<PamCpf> datas) {
        PamCpf res = null;
        for(PamCpf c: datas){
            if(c.getTime().equals(time)) {
                res = c;
                break;
            }
        }
        return res;
    }
    private PamJianpai GetJPByTime(String type, String time, String fieldName, List<PamJianpai> datas) {
        PamJianpai pamJP = null;
        for(PamJianpai jp: datas) {
            if(jp.getTime().equals(time)) {
                if (jp.getType().equals("BX") && type.equals("walk")) {
                    pamJP = jp;
                } else if (jp.getType().equals("ZXC") && (type.equals("ecycle") || type.equals("cycle"))) {
                    pamJP = jp;
                } else if (jp.getType().equals("GJ") && type.equals("GJ")) {
                    pamJP = jp;
                } else if (jp.getType().equals("DT") && type.equals("DT")) {
                    pamJP = jp;
                }
            }
        }
        return pamJP;
    }

    private Double GetObjectByFieldName(PamJianpai jp, String fieldName) throws IllegalAccessException {
        Field[] fs = jp.getClass().getDeclaredFields();
        Field field = null;
        for(Field f: fs) {
            if(f.getName().equals(fieldName)) {
                field = f;
                break;
            }
        }
        Object vo = null;
        Double val = 0.0;
        if (field != null) {
            field.setAccessible(true);
            vo = field.get(jp);
            val = Double.parseDouble(vo.toString());
        }
        return val;
    }

    private DataFhYx GetDataFhYx(DataCheckYx od, Double left, Double right){
        DataFhYx data = new DataFhYx();
        data.setFileId(od.getFileId());
        data.setGroupDate(od.getGroupDate());
        data.setTripCheckCode(od.getTripCheckCode());
        data.setTripType(od.getTripType());
        data.setUserCode(od.getUserCode());
        data.setTripCode(od.getTripCode());
        data.setCardType(od.getCardType());
        data.setDataSource(od.getDataSource());
        data.setSlineCode(od.getSlineCode());
        data.setSlineDir(od.getSlineDir());
        data.setSstationCode(od.getSstationCode());
        data.setSstationName(od.getSstationName());
        data.setStime(od.getStime());
        data.setSlng(od.getSlng());
        data.setSlat(od.getSlat());
        data.setElineCode(od.getElineCode());
        data.setElineDir(od.getElineDir());
        data.setEstationCode(od.getEstationCode());
        data.setEstationName(od.getEstationName());
        data.setEtime(od.getEtime());
        data.setElng(od.getElng());
        data.setElat(od.getElat());
        data.setAveSpeed(od.getAveSpeed());
        data.setMaxSpeed(od.getMaxSpeed());
        data.setTripDuraion(od.getTripDuraion());
        data.setTripLength(od.getTripLength());
        data.setTripCheckLength(od.getTripCheckLength());
        data.setCSend(od.getCSend());
        data.setCBaseSend(od.getCBaseSend());
        data.setCProjectSend(od.getCProjectSend());
        data.setCSendLeft(left);
        data.setCSendRight(right);
        return data;
    }

    private DataFhErr GetDataFhErr(DataCheckYx od, Double left, Double right) {
        DataFhErr data = new DataFhErr();
        data.setFileId(od.getFileId());
        data.setGroupDate(od.getGroupDate());
        data.setTripCheckCode(od.getTripCheckCode());
        data.setTripType(od.getTripType());
        data.setUserCode(od.getUserCode());
        data.setTripCode(od.getTripCode());
        data.setCardType(od.getCardType());
        data.setDataSource(od.getDataSource());
        data.setSlineCode(od.getSlineCode());
        data.setSlineDir(od.getSlineDir());
        data.setSstationCode(od.getSstationCode());
        data.setSstationName(od.getSstationName());
        data.setStime(od.getStime());
        data.setSlng(od.getSlng());
        data.setSlat(od.getSlat());
        data.setElineCode(od.getElineCode());
        data.setElineDir(od.getElineDir());
        data.setEstationCode(od.getEstationCode());
        data.setEstationName(od.getEstationName());
        data.setEtime(od.getEtime());
        data.setElng(od.getElng());
        data.setElat(od.getElat());
        data.setAveSpeed(od.getAveSpeed());
        data.setMaxSpeed(od.getMaxSpeed());
        data.setTripDuraion(od.getTripDuraion());
        data.setTripLength(od.getTripLength());
        data.setTripCheckLength(od.getTripCheckLength());
        data.setCSend(od.getCSend());
        data.setCBaseSend(od.getCBaseSend());
        data.setCProjectSend(od.getCProjectSend());
        data.setCSendLeft(left);
        data.setCSendRight(right);
        data.setErrType("C_ERROR");
        data.setDataStatus("");
        return data;
    }
}
