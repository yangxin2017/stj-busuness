package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataFhYx;

import java.util.List;

public interface DataFhYxService extends IService<DataFhYx> {
    public PageData<DataFhYx> GetPageData(Integer fileId, Integer pageIndex, Integer pageSize);
    public PageData<DataFhYx> GetPageData(String tripType, String stime, String etime, Integer pageIndex, Integer pageSize);
    public PageData<DataFhYx> GetPageData(String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize);
    public PageData<DataFhYx> GetPageData(String datasource, String stime, Integer pageIndex, Integer pageSize);

    public Double GetCNumber(String datasource, String time);
    public Double GetCNumber(String datasource, String time, String tripType);
    public Double GetCNumber(String datasource, String stime, String etime, String tripType);
    public Double GetCNumber(String datasource, String groupDate, String stime, String etime, String tripType);


    public Integer GetCountByDatasourceAndTime(String datasource, String groupDate);
    public Integer GetCountByFileId(Integer fileId);

    public void DeleteByGroupDate(String datasource, String groupDate);

    public void InsertBatch(List<DataFhYx> lists);
}
