package com.bjd.smartanalysis.service.data;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataCheckYx;
import com.bjd.smartanalysis.entity.data.DataFhYx;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DataCheckYxService extends IService<DataCheckYx> {
    public DataCheckYx GetOnlyData(String userCode, Date stime, String slng, String slat);
    public DataCheckYx GetOnlyData(String tripCheckCode);

    public Integer GetCountByFileId(Integer fileId);
    public Integer GetCountBySourceAndDate(String datasource, String groupDate);
    public List<DataCheckYx> GetListBySourceAndDate(String datasource, String groupDate);

    public PageData<DataCheckYx> GetListByFileId(String tripType, String stime, String etime, Integer pageIndex, Integer pageSize);
    public PageData<DataCheckYx> GetListByFileId(String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize);
    public PageData<DataCheckYx> GetListByFileId(Integer fileId, Integer pageIndex, Integer pageSize);
    public PageData<DataCheckYx> GetGroupCheckDataList(String datasource, String groupDate, Integer pageIndex, Integer pageSize);

    public PageData<String> GetGroupCheckData(String datasource, Integer pageIndex, Integer pageSize);
    public PageData<String> GetGroupCheckData(String datasource, String groupDate, Integer pageIndex, Integer pageSize);

    public List<Map<String, Object>> GetDayActiveUser();
    public List<Map<String, Object>> GetCxLGreen();
    public List<Map<String, Object>> GetHourUser();

    public Integer GetUserCount();
    public Integer GetTripCount();

    public List<DataCheckYx> GetListByTime(String datasource, String stime, String etime);


    public void InsertBatch(List<DataCheckYx> lists);
}
