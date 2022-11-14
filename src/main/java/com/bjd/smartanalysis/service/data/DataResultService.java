package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.data.DataResult;

import java.util.List;

public interface DataResultService extends IService<DataResult> {
    public DataResult GetBySourceAndDate(String dataSource, String groupDate);

    public List<DataResult> GetList(String dataSource, String stime, String etime);
    public List<DataResult> GetEqualList(String dataSource, String groupDate);

    public DataResult GetNewestOne();
}
