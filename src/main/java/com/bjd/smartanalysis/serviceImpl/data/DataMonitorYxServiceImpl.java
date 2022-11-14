package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataMonitorYx;
import com.bjd.smartanalysis.mapper.data.DataMonitorYxMapper;
import com.bjd.smartanalysis.service.data.DataMonitorYxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DataMonitorYxServiceImpl extends ServiceImpl<DataMonitorYxMapper, DataMonitorYx> implements DataMonitorYxService {
    @Autowired
    private DataMonitorYxMapper mapper;

    @Override
    public DataMonitorYx GetOnlyData(String userCode, Date stime, String slng, String slat) {
        QueryWrapper<DataMonitorYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_code", userCode);
        queryWrapper.eq("stime", stime);
        queryWrapper.eq("slng", slng);
        queryWrapper.eq("slat", slat);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public Integer GetCountByFileId(Integer fileId) {
        QueryWrapper<DataMonitorYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountByDatasourceAndTime(String datasource, String groupDate) {
        QueryWrapper<DataMonitorYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public void InsertBatch(List<DataMonitorYx> lists) {
        int i = 0, y = 0;
        for (DataMonitorYx yx: lists) {
            if (i > 0 && (i % 1000 == 0 || i == lists.size() - 1)) {
                mapper.insertBatchSomeColumn(lists.subList(y * 1000, i));
                y++;
            }
            i++;
        }
    }

    @Override
    public PageData<DataMonitorYx> GetListByFileId(Integer fileId, Integer pageIndex, Integer pageSize) {
        IPage<DataMonitorYx> pageMonitor = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataMonitorYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        pageMonitor = mapper.selectPage(pageMonitor, queryWrapper);
        PageData<DataMonitorYx> res = new PageData<>();
        res.setData(pageMonitor.getRecords());
        res.setTotal(pageMonitor.getTotal());
        return res;
    }

    @Override
    public PageData<DataMonitorYx> GetListByFileId(String tripType, String stime, String etime, Integer fileId, Integer pageIndex, Integer pageSize) {
        IPage<DataMonitorYx> pageMonitor = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataMonitorYx> queryWrapper = new QueryWrapper<>();
        if (fileId != null) {
            queryWrapper.eq("file_id", fileId);
        }
        if (tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        if (stime != null && !stime.equals("")) {
            queryWrapper.eq("date(stime)", stime);
        }
        if (etime != null && !etime.equals("")) {
            queryWrapper.eq("date(etime)", etime);
        }
        pageMonitor = mapper.selectPage(pageMonitor, queryWrapper);
        PageData<DataMonitorYx> res = new PageData<>();
        res.setData(pageMonitor.getRecords());
        res.setTotal(pageMonitor.getTotal());
        return res;
    }

    @Override
    public PageData<DataMonitorYx> GetListByFileId(String datasource, String tripType, String stime, String etime, Integer fileId, Integer pageIndex, Integer pageSize) {
        IPage<DataMonitorYx> pageMonitor = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataMonitorYx> queryWrapper = new QueryWrapper<>();
        if (fileId != null) {
            queryWrapper.eq("file_id", fileId);
        }
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.like("data_source", datasource);
        }
        if (tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        if (stime != null && !stime.equals("")) {
            queryWrapper.eq("date(stime)", stime);
        }
        if (etime != null && !etime.equals("")) {
            queryWrapper.eq("date(etime)", etime);
        }
        pageMonitor = mapper.selectPage(pageMonitor, queryWrapper);
        PageData<DataMonitorYx> res = new PageData<>();
        res.setData(pageMonitor.getRecords());
        res.setTotal(pageMonitor.getTotal());
        return res;
    }
}
