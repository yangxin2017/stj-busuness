package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataCheckErr;

import java.util.Date;

public interface DataCheckErrService extends IService<DataCheckErr> {
    public DataCheckErr GetOnlyData(String userCode, Date stime, String slng, String slat);
    public DataCheckErr GetOnlyData(String tripCheckCode);

    public Integer GetCountByFileId(Integer fileId);
    public Integer GetCountByFileId(Integer fileId, String errType);
    public Integer GetCountBySourceAndDate(String datasource, String groupdate);

    public PageData<DataCheckErr> GetDataByErrType(Integer fileId, String errType, Integer pageIndex, Integer pageSize);
    public PageData<DataCheckErr> GetDataByErrType(Integer fileId, String errType, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize);

    public void SetDataStatus(String dataId, String status);


    public Integer GetUserCount();
    public Integer GetTripCount();
}
