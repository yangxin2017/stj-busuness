package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataFhTask;

import java.util.List;

public interface DataFhTaskService extends IService<DataFhTask> {
    public DataFhTask GetTaskBySourceAndDate(String datasource, String groupDate);

    public PageData<DataFhTask> GetPageTask(String datasource, Integer pageIndex, Integer pageSize);
    public PageData<DataFhTask> GetPageTask(String datasource, String groupDate, Integer pageIndex, Integer pageSize);

    public List<DataFhTask> GetList(String datasource);
}
