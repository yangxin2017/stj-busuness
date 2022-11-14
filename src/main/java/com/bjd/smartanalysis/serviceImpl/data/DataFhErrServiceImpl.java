package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataFhErr;
import com.bjd.smartanalysis.mapper.data.DataFhErrMapper;
import com.bjd.smartanalysis.service.data.DataFhErrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataFhErrServiceImpl extends ServiceImpl<DataFhErrMapper, DataFhErr> implements DataFhErrService {
    @Autowired
    private DataFhErrMapper mapper;

    @Override
    public PageData<DataFhErr> GetDataByErrType(Integer fileId, String errType, Integer pageIndex, Integer pageSize) {
        IPage<DataFhErr> fhPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
        if (fileId != null) {
            queryWrapper.eq("file_id", fileId);
        }
        if(errType != null && !errType.equals("")) {
            queryWrapper.eq("err_type", errType);
        }
        fhPage = mapper.selectPage(fhPage, queryWrapper);

        PageData<DataFhErr> page = new PageData<>();
        page.setData(fhPage.getRecords());
        page.setTotal(fhPage.getTotal());
        return page;
    }

    @Override
    public PageData<DataFhErr> GetDataByErrType(String datasource, String time, String tripType, String errType, Integer pageIndex, Integer pageSize) {
        IPage<DataFhErr> fhPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if (tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        if(errType != null && !errType.equals("")) {
            queryWrapper.eq("err_type", errType);
        }
        if(time != null && !time.equals("")) {
            queryWrapper.eq("group_date", time);
        }
        fhPage = mapper.selectPage(fhPage, queryWrapper);

        PageData<DataFhErr> page = new PageData<>();
        page.setData(fhPage.getRecords());
        page.setTotal(fhPage.getTotal());
        return page;
    }

    @Override
    public PageData<DataFhErr> GetDataByErrType(String datasource, String groupDate, String stime, String etime, String tripType, String errType, Integer pageIndex, Integer pageSize) {
        IPage<DataFhErr> fhPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if (tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        if(errType != null && !errType.equals("")) {
            queryWrapper.eq("err_type", errType);
        }
        if (stime != null && !stime.equals("")) {
            queryWrapper.eq("date(stime)", stime);
        }
        if (etime != null && !etime.equals("")) {
            queryWrapper.eq("date(etime)", etime);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        fhPage = mapper.selectPage(fhPage, queryWrapper);

        PageData<DataFhErr> page = new PageData<>();
        page.setData(fhPage.getRecords());
        page.setTotal(fhPage.getTotal());
        return page;
    }

    @Override
    public void SetDataStatus(String dataId, String status) {
        UpdateWrapper<DataFhErr> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("data_status", status);
        updateWrapper.eq("id", dataId);
        mapper.update(null, updateWrapper);
    }

    @Override
    public Double GetCNumber(String datasource, String time) {
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();

        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(time != null && !time.equals("")) {
            queryWrapper.eq("group_date", time);
        }

        queryWrapper.ne("data_status", "忽略");
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
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();

        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(time != null && !time.equals("")) {
            queryWrapper.eq("group_date", time);
        }
        if(tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }

        queryWrapper.ne("data_status", "忽略");
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
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();

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

        queryWrapper.ne("data_status", "忽略");
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
    public Integer GetJZCount(String datasource, String time) {
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();

        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(time != null && !time.equals("")) {
            queryWrapper.eq("group_date", time);
        }

        queryWrapper.eq("data_status", "忽略");

        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountByDatasourceAndTime(String datasource, String groupDate) {
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountByFileId(Integer fileId, String errType) {
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
        if(fileId != null) {
            queryWrapper.eq("file_id", fileId);
        }
        if(errType != null && !errType.equals("")) {
            queryWrapper.eq("err_type", errType);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Double GetJZNumber(String datasource, String time) {
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(time != null && !time.equals("")) {
            queryWrapper.eq("group_date", time);
        }
        queryWrapper.eq("data_status", "忽略");
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
    public Double GetJZNumber(String datasource, String time, String tripType) {
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(time != null && !time.equals("")) {
            queryWrapper.eq("group_date", time);
        }
        if(tripType != null && !tripType.equals("")) {
            queryWrapper.like("trip_type", tripType);
        }
        queryWrapper.eq("data_status", "忽略");
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
    public Double GetJZNumber(String datasource, String stime, String etime, String tripType) {
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
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
        queryWrapper.eq("data_status", "忽略");
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
    public void DeleteByGroupDate(String datasource, String groupDate) {
        QueryWrapper<DataFhErr> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        mapper.delete(queryWrapper);
    }
}
