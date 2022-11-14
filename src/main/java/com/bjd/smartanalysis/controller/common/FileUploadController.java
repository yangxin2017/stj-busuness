package com.bjd.smartanalysis.controller.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.CommonUtil;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.DataFile;
import com.bjd.smartanalysis.entity.DataType;
import com.bjd.smartanalysis.entity.common.CmFile;
import com.bjd.smartanalysis.entity.common.CmFileType;
import com.bjd.smartanalysis.entity.data.*;
import com.bjd.smartanalysis.entity.flow.SysFlow;
import com.bjd.smartanalysis.service.common.CmFileService;
import com.bjd.smartanalysis.service.common.CmFileTypeService;
import com.bjd.smartanalysis.service.data.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

@RestController
@Api(value = "文件管理-com", tags = {"文件管理"})
@RequestMapping("filemanager")
public class FileUploadController {
    @Value("${file.upload.path}")
    private String basePath;

    @Autowired
    private CmFileTypeService fileTypeService;
    @Autowired
    private CmFileService fileService;
    @Autowired
    private DataMonitorDetailService monitorDetailService;
    @Autowired
    private DataMonitorYxService monitorYxService;
    @Autowired
    private DataMonitorErrService monitorErrService;
    @Autowired
    private DataCheckYxService checkYxService;
    @Autowired
    private DataCheckErrService checkErrService;

    @PostMapping("upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public ResponseData FileUpload(@RequestParam("file") MultipartFile file, Integer bid, String datasource) {
        if (file.isEmpty() || bid == null) {
            return ResponseData.FAIL("没有选择文件");
        }

        CmFileType dtype = fileTypeService.getById(bid);
        if (dtype == null) {
            return ResponseData.FAIL("数据类型不存在");
        }

        String name = file.getOriginalFilename();
        Long size = file.getSize();
        String stuf = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String relativePath = getCurDatePath();
        String newFilename = relativePath + "/" + UUID.randomUUID() + stuf;
        String filepath = basePath + "/" + newFilename;

        if(datasource == null || datasource.equals("")) {
            datasource = "高德";
        }

        try{
            FileUtil.mkParentDirs(filepath);
            file.transferTo(new File(filepath));
            ////////
            CmFile df = new CmFile();
            df.setCtime(new Date());
            df.setFileName(name);
            df.setSuffix(stuf);
            df.setFileSize(size.toString());
            df.setFilePath(newFilename);
            df.setTypeId(bid);
            df.setDataSource(datasource);
            df.setDataStatus(CmFileService.READY);
            fileService.save(df);

//            if(dtype.getName().equals("监测数据")) {
//                SaveMonitorDataTxt(filepath, df.getId(), datasource);
//            } else if(dtype.getName().equals("核证数据")) {
//                SaveCheckData(filepath, df.getId());
//            }

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseData.FAIL("文件上传失败");
        }
        return ResponseData.OK(newFilename);
    }

    private JSONObject InitMonitorCondition() {
        String str = ResourceUtil.readUtf8Str("classpath:monitor.json");
        return JSONObject.parseObject(str);
    }
    private String CheckFieldValid(JSONObject condition, Map<String, Object> dataObj) {
        String resultStr = "";
        Set<String> fields = condition.keySet();
        for (String field: fields) {
            JSONArray arr = condition.getJSONArray(field);
            Object val = dataObj.get(field);
            if (arr != null && arr.size() > 0) {
                for (int i=0;i<arr.size();i++) {
                    JSONObject jud = arr.getJSONObject(i);
                    String name = jud.getString("name");
                    if (name.equals("notnull")) {
                        if(val == null || val.equals("")){
                            resultStr = "NULL";
                            break;
                        }
                    }
                    if (name.equals("datetime")) {
                        if (!CommonUtil.isLegalDate(val.toString())) {
                            resultStr = "ERROR";
                            break;
                        }
                    }
                    if (name.equals("range-time")) {
                        Date curd = DateUtil.parse(val.toString());
                        long curtime = curd.getTime();
                        long min = jud.getLong("min");
                        long max = jud.getLong("max");
                        if (curtime < min || curtime > max) {
                            resultStr = "ERROR";
                            break;
                        }
                    }
                    if (name.equals("range")) {
                        double curval = Double.parseDouble(val.toString());
                        double min = jud.getLong("min");
                        double max = jud.getLong("max");
                        if (curval < min || curval > max) {
                            resultStr = "ERROR";
                            break;
                        }
                    }
                }
                if (resultStr.equals("NULL")){
                    break;
                }
            }
        }
        return resultStr;
    }

    private void SaveMonitorDataTxt(String filepath, Integer fileId, String datasource) {
        JSONObject monitorCondition = InitMonitorCondition();
        String contents = FileUtil.readString(filepath, Charset.forName("utf-8"));
        String[] lines = contents.split("\n");
        for (String l: lines) {
            String[] fields = l.split("\t");

            if (fields.length == 16) {
                Object tripType = fields[0];
                Object userCode = fields[1];
                Object tripCode = fields[2];
                Object stimestr = fields[3];
                Object slng = fields[5];
                Object slat = fields[6];
                Object etimestr = fields[7];
                Object elng = fields[9];
                Object elat = fields[10];
                Object aveSpeed = fields[11];
                Object maxSpeed = fields[12];
                Object tripLength = fields[13];
                Object tripDuration = fields[14];
                Object tripDetail = fields[15];

                Map<String, Object> obj = new HashMap<>();
                obj.put("出行里程", tripLength);

                String resstr = CheckFieldValid(monitorCondition, obj);
                Date stime = null;
                Date etime = null;
                if (resstr.equals("")) {
                    if(stimestr != null)
                        stime = DateUtil.parse(stimestr.toString());
                    if(etimestr != null)
                        etime = DateUtil.parse(etimestr.toString());
                    DataMonitorYx yxExist = monitorYxService.GetOnlyData(userCode.toString(), stime, slng.toString(), slat.toString());
                    if (yxExist != null) {
                        resstr = "REPEAT";
                    }
                } else {
                    if(stimestr != null)
                        stime = DateUtil.parse(stimestr.toString());
                    if(etimestr != null)
                        etime = DateUtil.parse(etimestr.toString());

                    DataMonitorErr errExist = monitorErrService.GetOnlyData(userCode.toString(), stime, slng.toString(), slat.toString());
                    if (errExist != null) {
                        resstr = "ERR-REPEAT";
                    }
                }

                if (resstr.equals("")) {
                    String groupDate = "";
                    if(stimestr != null && !stimestr.equals("")) {
                        String[] arr = stimestr.toString().split(" ");
                        String[] arrDate = arr[0].split("-");
                        groupDate = arrDate[0] + "-" + arrDate[1];
                    }
                    DataMonitorYx data = new DataMonitorYx();
                    data.setFileId(fileId);
                    data.setGroupDate(groupDate);
                    data.setDataSource(datasource);
                    data.setTripType(tripType.toString());
                    data.setUserCode(userCode.toString());
                    data.setTripCode(tripCode.toString());
                    data.setStime(stime);
                    data.setSlng(slng != null ? slng.toString() : "");
                    data.setSlat(slat != null ? slat.toString() : "");
                    data.setEtime(etime);
                    data.setElng(elng != null ? elng.toString() : "");
                    data.setElat(elat != null ? elat.toString() : "");
                    data.setAveSpeed(aveSpeed.toString());
                    data.setMaxSpeed(maxSpeed.toString());
                    data.setTripLength(tripLength.toString());
                    data.setTripDuration(tripDuration.toString());
                    String groupId = IdUtil.randomUUID();
                    data.setTripDetailId(groupId);
                    monitorYxService.save(data);


                    /////////
                    if(tripDetail != null && !tripDetail.equals("")) {
                        String[] datas = tripDetail.toString().split("|");
                        for(String ds: datas) {
                            String[] dsarr = ds.split(",");
                            if (dsarr.length == 5) {
                                DataMonitorDetail dataDetail = new DataMonitorDetail();
                                String dtimestr = dsarr[1];
                                String dslng = dsarr[2];
                                String dslat = dsarr[3];
                                String dspeed = dsarr[4];
                                Date dtime = DateUtil.parse(dtimestr);
                                ///////////////
                                dataDetail.setLat(dslat);
                                dataDetail.setLng(dslng);
                                dataDetail.setSpeed(dspeed);
                                dataDetail.setTime(dtime);
                                dataDetail.setGroupId(groupId);
                                monitorDetailService.save(dataDetail);
                            }
                        }
                    }
                } else {
                    String groupDate = "";
                    if(stimestr != null && !stimestr.equals("")) {
                        String[] arr = stimestr.toString().split(" ");
                        String[] arrDate = arr[0].split("-");
                        groupDate = arrDate[0] + "-" + arrDate[1];
                    }
                    /////
                    DataMonitorErr data = new DataMonitorErr();
                    data.setFileId(fileId);
                    data.setErrType(resstr);
                    data.setGroupDate(groupDate);
                    data.setDataSource(datasource);
                    data.setTripType(tripType.toString());
                    data.setUserCode(userCode.toString());
                    data.setTripCode(tripCode.toString());
                    data.setStime(stime);
                    data.setSlng(slng != null ? slng.toString() : "");
                    data.setSlat(slat != null ? slat.toString() : "");
                    data.setEtime(etime);
                    data.setElng(elng != null ? elng.toString() : "");
                    data.setElat(elat != null ? elat.toString() : "");
                    data.setAveSpeed(aveSpeed != null ? aveSpeed.toString() : "");
                    data.setMaxSpeed(maxSpeed != null ? maxSpeed.toString() : "");
                    data.setTripLength(tripLength != null ? tripLength.toString() : "");
                    data.setTripDuration(tripDuration != null ? tripDuration.toString() : "");
                    String groupId = IdUtil.randomUUID();
                    data.setTripDetailId(groupId);
                    monitorErrService.save(data);
                    /////////
                    if(tripDetail != null && !tripDetail.equals("")) {
                        String[] datas = tripDetail.toString().split("|");
                        for(String ds: datas) {
                            String[] dsarr = ds.split(",");
                            if (dsarr.length == 5) {
                                DataMonitorDetail dataDetail = new DataMonitorDetail();
                                String dtimestr = dsarr[1];
                                String dslng = dsarr[2];
                                String dslat = dsarr[3];
                                String dspeed = dsarr[4];
                                Date dtime = DateUtil.parse(dtimestr);
                                ///////////////
                                dataDetail.setLat(dslat);
                                dataDetail.setLng(dslng);
                                dataDetail.setSpeed(dspeed);
                                dataDetail.setTime(dtime);
                                dataDetail.setGroupId(groupId);
                                monitorDetailService.save(dataDetail);
                            }
                        }
                    }
                }

            }
            System.out.println(fields.length);
        }
    }

    private void SaveMonitorData(String filepath, Integer fileId, String datasource) {
        JSONObject monitorCondition = InitMonitorCondition();

        ExcelReader excelReader = ExcelUtil.getReader(ResourceUtil.getStream(filepath));
        List<Map<String, Object>> listAll = excelReader.readAll();
        for(Map<String, Object> obj : listAll) {
            Object tripType = obj.get("出行方式");
            Object userCode = obj.get("用户唯一标识");
            Object tripCode = obj.get("出行唯一识别码");
            Object stimestr = obj.get("起始时间");
            Object slng = obj.get("起始经度");
            Object slat = obj.get("起始纬度");
            Object etimestr = obj.get("结束时间");
            Object elng = obj.get("结束经度");
            Object elat = obj.get("结束纬度");
            Object aveSpeed = obj.get("平均速度");
            Object maxSpeed = obj.get("最大瞬时速度");
            Object tripLength = obj.get("出行里程");
            Object tripDuration = obj.get("行程时间");
            Object tripDetail = obj.get("轨迹详情");

            String resstr = CheckFieldValid(monitorCondition, obj);
            Date stime = null;
            Date etime = null;
            if (resstr.equals("")) {
                if(stimestr != null)
                    stime = DateUtil.parse(stimestr.toString());
                if(etimestr != null)
                    etime = DateUtil.parse(etimestr.toString());
                DataMonitorYx yxExist = monitorYxService.GetOnlyData(userCode.toString(), stime, slng.toString(), slat.toString());
                if (yxExist != null) {
                    resstr = "REPEAT";
                }
            } else {
                if(stimestr != null)
                    stime = DateUtil.parse(stimestr.toString());
                if(etimestr != null)
                    etime = DateUtil.parse(etimestr.toString());

                DataMonitorErr errExist = monitorErrService.GetOnlyData(userCode.toString(), stime, slng.toString(), slat.toString());
                if (errExist != null) {
                    resstr = "ERR-REPEAT";
                }
            }

            if (resstr.equals("")) {
                String groupDate = "";
                if(stimestr != null && !stimestr.equals("")) {
                    String[] arr = stimestr.toString().split(" ");
                    String[] arrDate = arr[0].split("-");
                    groupDate = arrDate[0] + "-" + arrDate[1];
                }
                DataMonitorYx data = new DataMonitorYx();
                data.setFileId(fileId);
                data.setGroupDate(groupDate);
                data.setDataSource(datasource);
                data.setTripType(tripType.toString());
                data.setUserCode(userCode.toString());
                data.setTripCode(tripCode.toString());
                data.setStime(stime);
                data.setSlng(slng != null ? slng.toString() : "");
                data.setSlat(slat != null ? slat.toString() : "");
                data.setEtime(etime);
                data.setElng(elng != null ? elng.toString() : "");
                data.setElat(elat != null ? elat.toString() : "");
                data.setAveSpeed(aveSpeed.toString());
                data.setMaxSpeed(maxSpeed.toString());
                data.setTripLength(tripLength.toString());
                data.setTripDuration(tripDuration.toString());
                String groupId = IdUtil.randomUUID();
                data.setTripDetailId(groupId);
                monitorYxService.save(data);


                /////////
                if(tripDetail != null && !tripDetail.equals("")) {
                    String[] datas = tripDetail.toString().split("|");
                    for(String ds: datas) {
                        String[] dsarr = ds.split(",");
                        if (dsarr.length == 5) {
                            DataMonitorDetail dataDetail = new DataMonitorDetail();
                            String dtimestr = dsarr[1];
                            String dslng = dsarr[2];
                            String dslat = dsarr[3];
                            String dspeed = dsarr[4];
                            Date dtime = DateUtil.parse(dtimestr);
                            ///////////////
                            dataDetail.setLat(dslat);
                            dataDetail.setLng(dslng);
                            dataDetail.setSpeed(dspeed);
                            dataDetail.setTime(dtime);
                            dataDetail.setGroupId(groupId);
                            monitorDetailService.save(dataDetail);
                        }
                    }
                }
            } else {
                String groupDate = "";
                if(stimestr != null && !stimestr.equals("")) {
                    String[] arr = stimestr.toString().split(" ");
                    String[] arrDate = arr[0].split("-");
                    groupDate = arrDate[0] + "-" + arrDate[1];
                }
                /////
                DataMonitorErr data = new DataMonitorErr();
                data.setFileId(fileId);
                data.setErrType(resstr);
                data.setGroupDate(groupDate);
                data.setDataSource(datasource);
                data.setTripType(tripType.toString());
                data.setUserCode(userCode.toString());
                data.setTripCode(tripCode.toString());
                data.setStime(stime);
                data.setSlng(slng != null ? slng.toString() : "");
                data.setSlat(slat != null ? slat.toString() : "");
                data.setEtime(etime);
                data.setElng(elng != null ? elng.toString() : "");
                data.setElat(elat != null ? elat.toString() : "");
                data.setAveSpeed(aveSpeed != null ? aveSpeed.toString() : "");
                data.setMaxSpeed(maxSpeed != null ? maxSpeed.toString() : "");
                data.setTripLength(tripLength != null ? tripLength.toString() : "");
                data.setTripDuration(tripDuration != null ? tripDuration.toString() : "");
                String groupId = IdUtil.randomUUID();
                data.setTripDetailId(groupId);
                monitorErrService.save(data);
                /////////
                if(tripDetail != null && !tripDetail.equals("")) {
                    String[] datas = tripDetail.toString().split("|");
                    for(String ds: datas) {
                        String[] dsarr = ds.split(",");
                        if (dsarr.length == 5) {
                            DataMonitorDetail dataDetail = new DataMonitorDetail();
                            String dtimestr = dsarr[1];
                            String dslng = dsarr[2];
                            String dslat = dsarr[3];
                            String dspeed = dsarr[4];
                            Date dtime = DateUtil.parse(dtimestr);
                            ///////////////
                            dataDetail.setLat(dslat);
                            dataDetail.setLng(dslng);
                            dataDetail.setSpeed(dspeed);
                            dataDetail.setTime(dtime);
                            dataDetail.setGroupId(groupId);
                            monitorDetailService.save(dataDetail);
                        }
                    }
                }
            }

        }
    }


    private JSONObject InitCheckCondition() {
        String str = ResourceUtil.readUtf8Str("classpath:check.json");
        return JSONObject.parseObject(str);
    }

    private void SaveCheckData(String filepath, Integer fileId) {
        JSONObject checkCondition = InitCheckCondition();
        CsvReader reader = CsvUtil.getReader();
        CsvData data = reader.read(FileUtil.file(filepath), Charset.forName("gbk"));
        List<CsvRow> rows = data.getRows();

        if(rows.size() > 0) {
            CsvRow header = rows.get(0);
            for(int i=1;i<rows.size();i++){
                CsvRow dr = rows.get(i);
                Map<String, Object> obj = new HashMap<>();
                for(int j = 0;j<header.size();j++) {
                    obj.put(header.get(j), dr.get(j));
                }
                ///////
                Object tripCheckCode = GetValueByKey(obj,"核验行程唯一识别码", "id");
                Object tripType = GetValueByKey(obj,"出行方式", "trip_mode");
                Object userCode = GetValueByKey(obj,"用户唯一标识", "user_id");
                Object tripCode = GetValueByKey(obj,"出行唯一识别码", "card_id");
                Object cardType = GetValueByKey(obj,"卡类型", "card_type");
                Object dataSource = GetValueByKey(obj,"数据来源平台", "data_source");
                Object slineCode = GetValueByKey(obj,"起始线路编号", "start_line_no");
                Object slineDir = GetValueByKey(obj,"起始线路方向", "start_line_dir");
                Object sstationCode = GetValueByKey(obj,"起始站牌编号", "start_stop_no");
                Object sstationName = GetValueByKey(obj,"起始站牌名称", "start_stop_name");
                Object stimestr = GetValueByKey(obj,"起始时间", "start_time");
                Boolean isok = CommonUtil.isLegalDate(stimestr.toString());
                Object slng = GetValueByKey(obj,"起始经度", "start_lon");
                Object slat = GetValueByKey(obj,"起始纬度", "start_lat");
                Object elineCode = GetValueByKey(obj,"结束线路编号", "end_line_no");
                Object elineDir = GetValueByKey(obj,"结束线路方向", "end_line_dir");
                Object estationCode = GetValueByKey(obj,"结束站牌编号", "end_stop_no");
                Object estationName = GetValueByKey(obj,"结束站牌名称", "end_stop_name");
                Object etimestr = GetValueByKey(obj,"结束时间", "end_time");
                Object elng = GetValueByKey(obj,"结束经度", "end_lon");
                Object elat = GetValueByKey(obj,"结束纬度", "end_lat");
                Object aveSpeed = GetValueByKey(obj,"平均速度", "speed_avg");
                Object maxSpeed = GetValueByKey(obj,"最大瞬时速度", "speed_max");
                Object tripDuraion = GetValueByKey(obj,"行程时间", "travel_time");
                Object tripLength = GetValueByKey(obj,"出行里程", "trip_distance");
                Object tripCheckLength = GetValueByKey(obj,"核验出行里程", "verify_trip_distance");
                Object cSend = GetValueByKey(obj,"碳减排量", "CERs");
                Object cBaseSend = GetValueByKey(obj,"基准碳排放量", "standard_c_let");
                Object cProjectSend = GetValueByKey(obj,"项目碳排放量", "c_let");
                Object date = GetValueByKey(obj,"项目碳排放量", "date");
                Object roadname = GetValueByKey(obj,"项目碳排放量", "roadname");
                Object link = GetValueByKey(obj,"项目碳排放量", "link");

                String resstr = CheckFieldValid(checkCondition, obj);
                Date stime = null;
                Date etime = null;
                if (resstr.equals("")) {
                    if(stimestr != null)
                        stime = DateUtil.parse(stimestr.toString());
                    if(etimestr != null)
                        etime = DateUtil.parse(etimestr.toString());
                    DataCheckYx yxExist = checkYxService.GetOnlyData(userCode.toString(), stime, slng.toString(), slat.toString());
                    if (yxExist != null) {
                        resstr = "REPEAT";
                    }
                } else {
                    if(stimestr != null)
                        stime = DateUtil.parse(stimestr.toString());
                    if(etimestr != null)
                        etime = DateUtil.parse(etimestr.toString());

                    DataCheckErr errExist = checkErrService.GetOnlyData(userCode.toString(), stime, slng.toString(), slat.toString());
                    if (errExist != null) {
                        resstr = "ERR-REPEAT";
                    }
                }
                //////////
                if (resstr.equals("")) {
                    String groupDate = "";
                    if (stimestr != null && !stimestr.equals("")) {
                        String[] arr = stimestr.toString().split(" ");
                        String[] arrDate = arr[0].split("/");
                        if(arrDate.length > 2) {
                            if (arrDate[1].length() <= 1) {
                                arrDate[1] = "0" + arrDate[1];
                            }
                            groupDate = arrDate[0] + "-" + arrDate[1];
                        } else {
                            arrDate = arr[0].split("-");
                            if (arrDate[1].length() <= 1) {
                                arrDate[1] = "0" + arrDate[1];
                            }
                            groupDate = arrDate[0] + "-" + arrDate[1];
                        }
                    }
                    DataCheckYx checkData = new DataCheckYx();
                    checkData.setGroupDate(groupDate);
                    checkData.setFileId(fileId);
                    checkData.setTripCheckCode(tripCheckCode != null ? tripCheckCode.toString() : "");
                    checkData.setTripType(tripType.toString());
                    checkData.setUserCode(userCode.toString());
                    checkData.setTripCode(tripCode.toString());
                    checkData.setCardType(cardType.toString());
                    checkData.setDataSource(dataSource.toString());
                    checkData.setSlineCode(slineCode.toString());
                    checkData.setSlineDir(slineDir.toString());
                    checkData.setSstationCode(sstationCode.toString());
                    checkData.setSstationName(sstationName.toString());
                    checkData.setStime(stime);
                    checkData.setSlng(slng.toString());
                    checkData.setSlat(slat.toString());
                    checkData.setElineCode(elineCode.toString());
                    checkData.setElineDir(elineDir.toString());
                    checkData.setEstationCode(estationCode.toString());
                    checkData.setEstationName(estationName.toString());
                    checkData.setEtime(etime);
                    checkData.setElng(elng.toString());
                    checkData.setElat(elat.toString());
                    checkData.setAveSpeed(aveSpeed.toString());
                    checkData.setMaxSpeed(maxSpeed.toString());
                    checkData.setTripDuraion(tripDuraion.toString());
                    checkData.setTripLength(tripLength.toString());
                    checkData.setTripCheckLength(tripCheckLength.toString());
                    checkData.setCSend(Double.parseDouble(cSend.toString()));
                    checkData.setCBaseSend(Double.parseDouble(cBaseSend.toString()));
                    checkData.setCProjectSend(Double.parseDouble(cProjectSend.toString()));
                    checkData.setDate(date.toString());
                    checkData.setRoadname(roadname.toString());
                    checkData.setLink(link.toString());
                    checkYxService.save(checkData);
                } else {
                    String groupDate = "";
                    if(stimestr != null && !stimestr.equals("")) {
                        String[] arr = stimestr.toString().split(" ");
                        String[] arrDate = arr[0].split("/");
                        if(arrDate.length > 2) {
                            if (arrDate[1].length() <= 1) {
                                arrDate[1] = "0" + arrDate[1];
                            }
                            groupDate = arrDate[0] + "-" + arrDate[1];
                        } else {
                            arrDate = arr[0].split("-");
                            if (arrDate[1].length() <= 1) {
                                arrDate[1] = "0" + arrDate[1];
                            }
                            groupDate = arrDate[0] + "-" + arrDate[1];
                        }
                    }
                    DataCheckErr checkData = new DataCheckErr();
                    checkData.setErrType(resstr);
                    checkData.setDataStatus("");
                    checkData.setFileId(fileId);
                    checkData.setGroupDate(groupDate);
                    checkData.setTripCheckCode(tripCheckCode.toString());
                    checkData.setTripType(tripType.toString());
                    checkData.setUserCode(userCode.toString());
                    checkData.setTripCode(tripCode.toString());
                    checkData.setCardType(cardType.toString());
                    checkData.setDataSource(dataSource.toString());
                    checkData.setSlineCode(slineCode.toString());
                    checkData.setSlineDir(slineDir.toString());
                    checkData.setSstationCode(sstationCode.toString());
                    checkData.setSstationName(sstationName.toString());
                    checkData.setStime(stime);
                    checkData.setSlng(slng.toString());
                    checkData.setSlat(slat.toString());
                    checkData.setElineCode(elineCode.toString());
                    checkData.setElineDir(elineDir.toString());
                    checkData.setEstationCode(estationCode.toString());
                    checkData.setEstationName(estationName.toString());
                    checkData.setEtime(etime);
                    checkData.setElng(elng.toString());
                    checkData.setElat(elat.toString());
                    checkData.setAveSpeed(aveSpeed.toString());
                    checkData.setMaxSpeed(maxSpeed.toString());
                    checkData.setTripDuraion(tripDuraion.toString());
                    checkData.setTripLength(tripLength.toString());
                    checkData.setTripCheckLength(tripCheckLength.toString());
                    checkData.setCSend((cSend != null && !cSend.toString().equals("")) ? Double.parseDouble(cSend.toString()) : 0);
                    checkData.setCBaseSend((cBaseSend != null && !cBaseSend.toString().equals("")) ? Double.parseDouble(cBaseSend.toString()) : 0);
                    checkData.setCProjectSend((cProjectSend != null && !cProjectSend.toString().equals("")) ? Double.parseDouble(cProjectSend.toString()) : 0);
                    checkData.setDate(date.toString());
                    checkData.setRoadname(roadname.toString());
                    checkData.setLink(link.toString());
                    checkErrService.save(checkData);
                }
            }
        }
    }

    private Object GetValueByKey(Map<String, Object> obj, String name1, String name2) {
        Object val = obj.get(name1);
        if (val == null) {
            val = obj.get(name2);
        }
        return val;
    }

    private String getCurDatePath() {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer day = calendar.get(Calendar.DATE);
        String monthstr = month < 10 ? "0" + month : month.toString();
        String daystr = day < 10 ? "0" + day : day.toString();

        return year + "/" + monthstr + "/" + daystr;
    }

}
