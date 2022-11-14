package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataMonitorYx;

import java.util.Date;
import java.util.List;

public interface DataMonitorYxService extends IService<DataMonitorYx> {
    public DataMonitorYx GetOnlyData(String userCode, Date stime, String slng, String slat);

    public Integer GetCountByFileId(Integer fileId);
    public Integer GetCountByDatasourceAndTime(String datasource, String groupDate);

    public void InsertBatch(List<DataMonitorYx> lists);

    public PageData<DataMonitorYx> GetListByFileId(Integer fileId, Integer pageIndex, Integer pageSize);
    public PageData<DataMonitorYx> GetListByFileId(String tripType, String stime, String etime, Integer fileId, Integer pageIndex, Integer pageSize);
    public PageData<DataMonitorYx> GetListByFileId(String datasource, String tripType, String stime, String etime, Integer fileId, Integer pageIndex, Integer pageSize);
}
