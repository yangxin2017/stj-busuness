package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataFhErr;

public interface DataFhErrService extends IService<DataFhErr> {
    public PageData<DataFhErr> GetDataByErrType(Integer fileId, String errType, Integer pageIndex, Integer pageSize);
    public PageData<DataFhErr> GetDataByErrType(String datasource, String time, String tripType, String errType, Integer pageIndex, Integer pageSize);
    public PageData<DataFhErr> GetDataByErrType(String datasource, String groupDate, String stime, String etime, String tripType, String errType, Integer pageIndex, Integer pageSize);

    public void SetDataStatus(String dataId, String status);

    public Double GetCNumber(String datasource, String time);
    public Double GetCNumber(String datasource, String time, String tripType);
    public Double GetCNumber(String datasource, String stime, String etime, String tripType);

    public Integer GetJZCount(String datasource, String time);
    public Integer GetCountByDatasourceAndTime(String datasource, String groupDate);
    public Integer GetCountByFileId(Integer fileId, String errType);

    public Double GetJZNumber(String datasource, String time);
    public Double GetJZNumber(String datasource, String time, String tripType);
    public Double GetJZNumber(String datasource, String stime, String etime, String tripType);

    public void DeleteByGroupDate(String datasource, String groupDate);
}
