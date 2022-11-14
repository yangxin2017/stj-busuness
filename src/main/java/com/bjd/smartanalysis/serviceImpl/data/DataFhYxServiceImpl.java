package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataFhYx;
import com.bjd.smartanalysis.entity.data.DataMonitorYx;
import com.bjd.smartanalysis.mapper.data.DataFhYxMapper;
import com.bjd.smartanalysis.service.data.DataFhYxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataFhYxServiceImpl extends ServiceImpl<DataFhYxMapper, DataFhYx> implements DataFhYxService {
    @Autowired
    private DataFhYxMapper mapper;

    @Override
    public PageData<DataFhYx> GetPageData(Integer fileId, Integer pageIndex, Integer pageSize) {
        IPage<DataFhYx> fhPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        fhPage = mapper.selectPage(fhPage, queryWrapper);

        PageData<DataFhYx> page = new PageData<>();
        page.setData(fhPage.getRecords());
        page.setTotal(fhPage.getTotal());
        return page;
    }

    @Override
    public PageData<DataFhYx> GetPageData(String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        IPage<DataFhYx> fhPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();

        if (tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        if (stime != null && !stime.equals("")) {
            queryWrapper.eq("date(stime)", stime);
        }
        if (etime != null && !etime.equals("")) {
            queryWrapper.eq("date(etime)", etime);
        }
        fhPage = mapper.selectPage(fhPage, queryWrapper);

        PageData<DataFhYx> page = new PageData<>();
        page.setData(fhPage.getRecords());
        page.setTotal(fhPage.getTotal());
        return page;
    }

    @Override
    public PageData<DataFhYx> GetPageData(String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        IPage<DataFhYx> fhPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();

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
        fhPage = mapper.selectPage(fhPage, queryWrapper);

        PageData<DataFhYx> page = new PageData<>();
        page.setData(fhPage.getRecords());
        page.setTotal(fhPage.getTotal());
        return page;
    }

    @Override
    public PageData<DataFhYx> GetPageData(String datasource, String stime, Integer pageIndex, Integer pageSize) {
        IPage<DataFhYx> fhPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();

        if (datasource != null && !datasource.equals("")) {
            queryWrapper.like("data_source", datasource);
        }
        if (stime != null && !stime.equals("")) {
            queryWrapper.eq("group_date", stime);
        }
        fhPage = mapper.selectPage(fhPage, queryWrapper);

        PageData<DataFhYx> page = new PageData<>();
        page.setData(fhPage.getRecords());
        page.setTotal(fhPage.getTotal());
        return page;
    }

    @Override
    public Double GetCNumber(String datasource, String time) {
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(time != null && !time.equals("")) {
            queryWrapper.eq("group_date", time);
        }
        queryWrapper.select("sum(c_send_right) as cc");
        List<Map<String, Object>> maps = mapper.selectMaps(queryWrapper);
        Double sum = 0.0;
        for(Map<String, Object> m: maps) {
            if(m != null) {
                Double s = (Double) m.get("cc");
                sum += s;
            }
        }
        return sum;
    }

    @Override
    public Double GetCNumber(String datasource, String time, String tripType) {
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(time != null && !time.equals("")) {
            queryWrapper.eq("group_date", time);
        }
        if(tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        queryWrapper.select("sum(c_send_right) as cc");
        List<Map<String, Object>> maps = mapper.selectMaps(queryWrapper);
        Double sum = 0.0;
        for(Map<String, Object> m: maps) {
            if(m != null) {
                Double s = (Double) m.get("cc");
                sum += s;
            }
        }
        return sum;
    }

    @Override
    public Double GetCNumber(String datasource, String stime, String etime, String tripType) {
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(stime != null && !stime.equals("")) {
            queryWrapper.ge("group_date", stime);
        }
        if(etime != null && !etime.equals("")) {
            queryWrapper.lt("group_date", etime);
        }
        if(tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        queryWrapper.select("sum(c_send_right) as cc");
        List<Map<String, Object>> maps = mapper.selectMaps(queryWrapper);
        Double sum = 0.0;
        for(Map<String, Object> m: maps) {
            if(m != null) {
                Double s = (Double) m.get("cc");
                sum += s;
            }
        }
        return sum;
    }

    @Override
    public Double GetCNumber(String datasource, String groupDate, String stime, String etime, String tripType) {
        return null;
    }

    @Override
    public Integer GetCountByDatasourceAndTime(String datasource, String groupDate) {
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountByFileId(Integer fileId) {
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();
        if(fileId != null) {
            queryWrapper.eq("file_id", fileId);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public void DeleteByGroupDate(String datasource, String groupDate) {
        QueryWrapper<DataFhYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        mapper.delete(queryWrapper);
    }

    @Override
    public void InsertBatch(List<DataFhYx> lists) {
        int insertCount = 500;
        int i = 0, y = 0;
        for (DataFhYx yx: lists) {
            if (i > 0 && (i % insertCount == 0 || i == lists.size() - 1)) {
                mapper.insertBatchSomeColumn(lists.subList(y * insertCount, i));
                y++;
            }
            i++;
        }
    }
}
