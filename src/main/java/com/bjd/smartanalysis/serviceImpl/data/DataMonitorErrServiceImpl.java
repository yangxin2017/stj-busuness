package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataMonitorErr;
import com.bjd.smartanalysis.mapper.data.DataMonitorErrMapper;
import com.bjd.smartanalysis.service.data.DataMonitorErrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DataMonitorErrServiceImpl extends ServiceImpl<DataMonitorErrMapper, DataMonitorErr> implements DataMonitorErrService {
    @Autowired
    private DataMonitorErrMapper mapper;

    @Override
    public DataMonitorErr GetOnlyData(String userCode, Date stime, String slng, String slat) {
        QueryWrapper<DataMonitorErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_code", userCode);
        queryWrapper.eq("stime", stime);
        queryWrapper.eq("slng", slng);
        queryWrapper.eq("slat", slat);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public Integer GetCountByFileId(Integer fileId) {
        QueryWrapper<DataMonitorErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountByFileId(Integer fileId, String errType) {
        QueryWrapper<DataMonitorErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        if (errType != null && !errType.equals("")) {
            queryWrapper.eq("err_type", errType);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountByDatasourceAndTime(String datasource, String groupDate) {
        QueryWrapper<DataMonitorErr> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public PageData<DataMonitorErr> GetDataByErrType(Integer fileId, String errType, Integer pageIndex, Integer pageSize) {
        IPage<DataMonitorErr> pageMonitor = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataMonitorErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        queryWrapper.eq("err_type", errType);
        pageMonitor = mapper.selectPage(pageMonitor, queryWrapper);
        PageData<DataMonitorErr> res = new PageData<>();
        res.setData(pageMonitor.getRecords());
        res.setTotal(pageMonitor.getTotal());
        return res;
    }

    @Override
    public PageData<DataMonitorErr> GetAllData(Integer fileId, String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        IPage<DataMonitorErr> pageMonitor = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataMonitorErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);

        if (tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if (stime != null && !stime.equals("")) {
            queryWrapper.eq("date(stime)", stime);
        }
        if (etime != null && !etime.equals("")) {
            queryWrapper.eq("date(etime)", etime);
        }

        pageMonitor = mapper.selectPage(pageMonitor, queryWrapper);
        PageData<DataMonitorErr> res = new PageData<>();
        res.setData(pageMonitor.getRecords());
        res.setTotal(pageMonitor.getTotal());
        return res;
    }

    @Override
    public void SetDataStatus(String dataId, String status) {
        UpdateWrapper<DataMonitorErr> queryWrapper = new UpdateWrapper<>();
        queryWrapper.eq("id", dataId);
        queryWrapper.set("data_status", status);
        mapper.update(null, queryWrapper);
    }
}
