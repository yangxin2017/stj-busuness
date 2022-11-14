package com.bjd.smartanalysis.config.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.CommonUtil;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.common.CmFile;
import com.bjd.smartanalysis.entity.data.*;
import com.bjd.smartanalysis.service.common.CmFileService;
import com.bjd.smartanalysis.service.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class TaskService {
    @Value("${file.upload.path}")
    private String basePath;

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
    @Autowired
    private CmFileService fileService;
    @Autowired
    private DataFhTaskService taskService;

    @Autowired
    private UserCountService userCountService;
    @Autowired
    private TriptypeCountService triptypeCountService;
    @Autowired
    private HourCountService hourCountService;


    @Async
    public void excutVoidTask(CmFile file) {
        if (file != null) {
            if (file.getTypeId() == 1) {
//                file.setDataStatus(CmFileService.ING);
//                fileService.updateById(file);

                SaveMonitorDataTxt(basePath + "/" + file.getFilePath(), file.getId(), file.getDataSource());

                file.setDataStatus(CmFileService.OVER);
                fileService.updateById(file);

                System.out.println("插入数据完毕：" + file.getFileName());

            } else if (file.getTypeId() == 2) {
//                file.setDataStatus(CmFileService.ING);
//                fileService.updateById(file);

                SaveCheckData(basePath + "/" + file.getFilePath(), file.getId());

                file.setDataStatus(CmFileService.OVER);
                fileService.updateById(file);
                
                System.out.println("插入数据完毕：" + file.getFileName());
                /////
            }
        }
    }

    private void SaveMonitorDataTxt(String filepath, Integer fileId, String datasource) {
        JSONObject monitorCondition = InitMonitorCondition();
        String contents = FileUtil.readString(filepath, Charset.forName("utf-8"));
        String[] lines = contents.split("\n");

        List<DataMonitorYx> yxDatas = new ArrayList<>();

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
                    //monitorYxService.save(data);
                    yxDatas.add(data);

                    /////////
//                    if(tripDetail != null && !tripDetail.equals("")) {
//                        String[] datas = tripDetail.toString().split("|");
//                        for(String ds: datas) {
//                            String[] dsarr = ds.split(",");
//                            if (dsarr.length == 5) {
//                                DataMonitorDetail dataDetail = new DataMonitorDetail();
//                                String dtimestr = dsarr[1];
//                                String dslng = dsarr[2];
//                                String dslat = dsarr[3];
//                                String dspeed = dsarr[4];
//                                Date dtime = DateUtil.parse(dtimestr);
//                                ///////////////
//                                dataDetail.setLat(dslat);
//                                dataDetail.setLng(dslng);
//                                dataDetail.setSpeed(dspeed);
//                                dataDetail.setTime(dtime);
//                                dataDetail.setGroupId(groupId);
//                                monitorDetailService.save(dataDetail);
//                            }
//                        }
//                    }
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

        System.out.println(new Date().getTime());
        monitorYxService.InsertBatch(yxDatas);
        System.out.println(new Date().getTime());
    }

    private boolean isHaveYx(List<DataCheckYx> lists, String tripCode) {
        boolean ish = false;
        for(DataCheckYx y: lists) {
            if (y.getTripCheckCode() != null && y.getTripCheckCode().equals(tripCode)) {
                ish = true;
                break;
            }
        }
        return ish;
    }

    private void SaveCheckData(String filepath, Integer fileId) {
        JSONObject checkCondition = InitCheckCondition();
        CsvReader reader = CsvUtil.getReader();

        CsvData data = reader.read(FileUtil.file(filepath), Charset.forName("gbk"));
        List<CsvRow> rows = data.getRows();
        if(rows.size() > 0) {
            CsvRow dr = rows.get(1);
            String ds = dr.get(5);
            if (!ds.equals("百度") && !ds.equals("高德")) {
                data = reader.read(FileUtil.file(filepath), Charset.forName("utf-8"));
                rows = data.getRows();
            }
        }

        List<DataCheckYx> listsYx = new ArrayList<>();
        List<DataCheckErr> listsErr = new ArrayList<>();
        Date dataTime = new Date();

        Map<String, String> checkMap = new HashMap<>();
        Map<String, Integer> userMap = new HashMap<>();
        Map<String, Integer> tripMap = new HashMap<>();
        Map<String, Integer> hourMap = new HashMap<>();

        List<DataCheckYx> exists = new ArrayList<>();

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

//                    if (exists.size() <= 0) {
//                        String et = DateUtil.format(stime, "yyyy-MM-dd");
//                        String st = DateUtil.format(DateUtil.offsetHour(stime, -8), "yyyy-MM-dd");
//                        exists = checkYxService.GetListByTime(dataSource.toString(), st, et);
//                    }

//                    boolean yxExist = false;//isHaveYx(exists, tripCheckCode.toString());
//                    if (yxExist) {
//                        resstr = "REPEAT";
//                    }
                } else {
                    if(stimestr != null)
                        stime = DateUtil.parse(stimestr.toString());
                    if(etimestr != null)
                        etime = DateUtil.parse(etimestr.toString());

//                    DataCheckErr errExist = checkErrService.GetOnlyData(tripCheckCode.toString());
//                    if (errExist != null) {
//                        resstr = "ERR-REPEAT";
//                    }
                }
                if (stime != null) {
                    dataTime = stime;
                }
                //////////
                if (resstr.equals("")) {
                    String groupDate = "";
                    if (stimestr != null && !stimestr.equals("")) {
                        String[] arr = stimestr.toString().split(" ");
                        if (arr.length > 0) {
                            String[] arrDate = arr[0].split("/");
                            if (arrDate.length > 2) {
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
                    checkData.setDate(date != null ? date.toString() : "");
                    checkData.setRoadname(roadname != null ? roadname.toString() : "");
                    checkData.setLink(link != null ? link.toString() : "");
//                    checkYxService.save(checkData);
                    listsYx.add(checkData);

                    String key = groupDate + "#" + dataSource.toString();
                    if (!checkMap.containsKey(key)) {
                        checkMap.put(key, "OK");
                    }
                    if(stime != null) {
                        String timekey = DateUtil.format(stime, "yyyy-MM-dd");
                        Integer num = userMap.getOrDefault(timekey, 0);
                        num += 1;
                        userMap.put(timekey, num);
                        ///////////////////////////
                        String hourkey = DateUtil.format(stime, "HH");
                        Integer numhour = hourMap.getOrDefault(hourkey, 0);
                        numhour += 1;
                        hourMap.put(hourkey, numhour);
                    }
                    String keytrip = tripType.toString();
                    if (keytrip != null && !keytrip.equals("")) {
                        Integer tripnum = tripMap.getOrDefault(keytrip, 0);
                        tripnum += 1;
                        tripMap.put(keytrip, tripnum);
                    }
                } else {
                    String groupDate = "";
                    if(stimestr != null && !stimestr.equals("")) {
                        String[] arr = stimestr.toString().split(" ");
                        if (arr.length > 0) {
                            String[] arrDate = arr[0].split("/");
                            if (arrDate.length > 2) {
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
//                    checkErrService.save(checkData);
                    listsErr.add(checkData);
                }
            }
        }

        System.out.println("deal ok ====, start insert....");
        checkYxService.InsertBatch(listsYx);
        checkErrService.saveBatch(listsErr, 500);

        // 更新文件字段
        CmFile f = fileService.getById(fileId);
        if (f != null) {
            f.setDataTime(dataTime);
            f.setErrCount(listsErr.size());
            f.setYxCount(listsYx.size());
            f.setTotalCount(listsErr.size() + listsYx.size());
            fileService.updateById(f);
        }

        System.out.println("保存任务列表----开始");
        synchronized (fileId.toString().intern()) {
            Set<String> maps = checkMap.keySet();
            for (String k : maps) {
                String[] arr = k.split("#");
                String groupDate = arr[0];
                String datasource = arr[1];
                DataFhTask task = taskService.GetTaskBySourceAndDate(datasource, groupDate);

                Integer yxCount = checkYxService.GetCountBySourceAndDate(datasource, groupDate);
                Integer errCount = checkErrService.GetCountBySourceAndDate(datasource, groupDate);
                if (task == null) {
                    task = new DataFhTask();
                    task.setDataSource(datasource);
                    task.setGroupDate(groupDate);
                    task.setStatus("未复核");
                    task.setYxCount(yxCount);
                    task.setErrCount(errCount);
                    task.setTotalCount(yxCount + errCount);
                    taskService.save(task);
                } else {
                    task.setYxCount(yxCount);
                    task.setErrCount(errCount);
                    task.setTotalCount(yxCount + errCount);
                    taskService.updateById(task);
                }
            }
            System.out.println("保存任务列表----结束");

            System.out.println("保存用户数量");
            Set<String> timemaps = userMap.keySet();
            for (String k : timemaps) {
                UserCount uc = userCountService.GetByTime(k);
                if (uc == null) {
                    uc = new UserCount();
                    uc.setDatestr(k);
                    uc.setNum(userMap.getOrDefault(k, 0).toString());
                    userCountService.save(uc);
                } else {
                    Integer ec = Integer.parseInt(uc.getNum());
                    ec += userMap.getOrDefault(k, 0);
                    uc.setNum(ec.toString());
                    userCountService.updateById(uc);
                }
            }
            System.out.println("保存不同方式数量");
            Set<String> tripmaps = tripMap.keySet();
            for (String k : tripmaps) {
                TriptypeCount tc = triptypeCountService.GetByType(k);
                if (tc == null) {
                    tc = new TriptypeCount();
                    tc.setNum(tripMap.getOrDefault(k, 0).toString());
                    tc.setTripType(k);
                    triptypeCountService.save(tc);
                } else {
                    Integer ec = Integer.parseInt(tc.getNum());
                    ec += tripMap.getOrDefault(k, 0);
                    tc.setNum(ec.toString());
                    triptypeCountService.updateById(tc);
                }
            }
            System.out.println("保存小时数量");
            Set<String> hourmaps = hourMap.keySet();
            for (String k : hourmaps) {
                HourCount hc = hourCountService.GetByHour(k);
                if (hc == null) {
                    hc = new HourCount();
                    hc.setHstr(k);
                    hc.setNum(hourMap.getOrDefault(k, 0).toString());
                    hourCountService.save(hc);
                } else {
                    Integer ec = Integer.parseInt(hc.getNum());
                    ec += hourMap.getOrDefault(k, 0);
                    hc.setNum(ec.toString());
                    hourCountService.updateById(hc);
                }
            }
            System.out.println("OK---end");
        }
    }

    private Object GetValueByKey(Map<String, Object> obj, String name1, String name2) {
        Object val = obj.get(name1);
        if (val == null) {
            val = obj.get(name2);
        }
        return val;
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

    private JSONObject InitCheckCondition() {
        String str = ResourceUtil.readUtf8Str("classpath:check.json");
        return JSONObject.parseObject(str);
    }
}
