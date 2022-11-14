package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataMonitorErr;
import com.bjd.smartanalysis.entity.data.DataMonitorYx;

import java.util.Date;

public interface DataMonitorErrService extends IService<DataMonitorErr> {
    public DataMonitorErr GetOnlyData(String userCode, Date stime, String slng, String slat);
    public Integer GetCountByFileId(Integer fileId);
    public Integer GetCountByFileId(Integer fileId, String errType);
    public Integer GetCountByDatasourceAndTime(String datasource, String groupDate);

    public PageData<DataMonitorErr> GetDataByErrType(Integer fileId, String errType, Integer pageIndex, Integer pageSize);
    public PageData<DataMonitorErr> GetAllData(Integer fileId, String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize);

    public void SetDataStatus(String dataId, String status);
}
