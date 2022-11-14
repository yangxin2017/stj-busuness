package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataFhTask;
import com.bjd.smartanalysis.mapper.data.DataFhTaskMapper;
import com.bjd.smartanalysis.service.data.DataFhTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataFhTaskServiceImpl extends ServiceImpl<DataFhTaskMapper, DataFhTask> implements DataFhTaskService {
    @Autowired
    private DataFhTaskMapper mapper;

    @Override
    public DataFhTask GetTaskBySourceAndDate(String datasource, String groupDate) {
        QueryWrapper<DataFhTask> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public PageData<DataFhTask> GetPageTask(String datasource, Integer pageIndex, Integer pageSize) {
        IPage<DataFhTask> taskPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhTask> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        taskPage = mapper.selectPage(taskPage, queryWrapper);

        PageData<DataFhTask> data = new PageData<>();
        data.setTotal(taskPage.getTotal());
        data.setData(taskPage.getRecords());
        return data;
    }

    @Override
    public PageData<DataFhTask> GetPageTask(String datasource, String groupDate, Integer pageIndex, Integer pageSize) {
        IPage<DataFhTask> taskPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhTask> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if (groupDate != null && !groupDate.equals("")) {
            String[] arr = groupDate.split("-");
            if (arr.length > 1) {
                if (arr[1].startsWith("0")) {
                    arr[1] = arr[1].replace("0", "");
                }
            }
            String nstr = arr[0] + "-" + arr[1];
            queryWrapper.eq("group_date", groupDate).or().eq("group_date", nstr);
        }
        taskPage = mapper.selectPage(taskPage, queryWrapper);

        PageData<DataFhTask> data = new PageData<>();
        data.setTotal(taskPage.getTotal());
        data.setData(taskPage.getRecords());
        return data;
    }

    @Override
    public List<DataFhTask> GetList(String datasource) {
        QueryWrapper<DataFhTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_source", datasource);

        return mapper.selectList(queryWrapper);
    }
}
