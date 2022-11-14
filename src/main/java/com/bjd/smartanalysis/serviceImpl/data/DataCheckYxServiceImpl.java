package com.bjd.smartanalysis.serviceImpl.data;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataCheckYx;
import com.bjd.smartanalysis.entity.data.DataFhYx;
import com.bjd.smartanalysis.mapper.data.DataCheckYxMapper;
import com.bjd.smartanalysis.service.data.DataCheckYxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DataCheckYxServiceImpl extends ServiceImpl<DataCheckYxMapper, DataCheckYx> implements DataCheckYxService {
    @Autowired
    private DataCheckYxMapper mapper;

    @Override
    public DataCheckYx GetOnlyData(String userCode, Date stime, String slng, String slat) {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_code", userCode);
        queryWrapper.eq("stime", stime);
        queryWrapper.eq("slng", slng);
        queryWrapper.eq("slat", slat);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public DataCheckYx GetOnlyData(String tripCheckCode) {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trip_check_code", tripCheckCode);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public Integer GetCountByFileId(Integer fileId) {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountBySourceAndDate(String datasource, String groupDate) {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public List<DataCheckYx> GetListBySourceAndDate(String datasource, String groupDate) {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        return mapper.selectList(queryWrapper);
    }

    @Override
    public PageData<DataCheckYx> GetListByFileId(String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        IPage<DataCheckYx> checkPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        if (tripType != null && !tripType.equals("")) {
            queryWrapper.eq("trip_type", tripType);
        }
        if (stime != null && !stime.equals("")) {
            queryWrapper.eq("date(stime)", stime);
        }
        if (etime != null && !etime.equals("")) {
            queryWrapper.eq("date(etime)", etime);
        }

        PageData<DataCheckYx> res = new PageData<>();
        checkPage = mapper.selectPage(checkPage, queryWrapper);
        res.setTotal(checkPage.getTotal());
        res.setData(checkPage.getRecords());
        return res;
    }

    @Override
    public PageData<DataCheckYx> GetListByFileId(String datasource, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        IPage<DataCheckYx> checkPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if (tripType != null && !tripType.equals("")) {
            queryWrapper.eq("trip_type", tripType);
        }
        if (stime != null && !stime.equals("")) {
            queryWrapper.eq("date(stime)", stime);
        }
        if (etime != null && !etime.equals("")) {
            queryWrapper.eq("date(etime)", etime);
        }
        PageData<DataCheckYx> res = new PageData<>();
        checkPage = mapper.selectPage(checkPage, queryWrapper);
        res.setTotal(checkPage.getTotal());
        res.setData(checkPage.getRecords());
        return res;
    }

    @Override
    public PageData<DataCheckYx> GetListByFileId(Integer fileId, Integer pageIndex, Integer pageSize) {
        IPage<DataCheckYx> checkPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        PageData<DataCheckYx> res = new PageData<>();
        checkPage = mapper.selectPage(checkPage, queryWrapper);
        res.setTotal(checkPage.getTotal());
        res.setData(checkPage.getRecords());
        return res;
    }

    @Override
    public PageData<DataCheckYx> GetGroupCheckDataList(String datasource, String groupDate, Integer pageIndex, Integer pageSize) {
        IPage<DataCheckYx> checkPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("group_date");
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupDate != null && !groupDate.equals("")) {
            queryWrapper.eq("group_date", groupDate);
        }
        checkPage = mapper.selectPage(checkPage, queryWrapper);

        PageData<DataCheckYx> res = new PageData<>();
        checkPage = mapper.selectPage(checkPage, queryWrapper);
        res.setTotal(checkPage.getTotal());
        res.setData(checkPage.getRecords());
        return res;
    }

    @Override
    public PageData<String> GetGroupCheckData(String datasource, Integer pageIndex, Integer pageSize) {
        IPage<DataCheckYx> checkPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("group_date");
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        queryWrapper.groupBy("group_date");
        checkPage = mapper.selectPage(checkPage, queryWrapper);

        PageData<String> res = new PageData<>();
        List<String> dates = new ArrayList<>();
        for(DataCheckYx yx: checkPage.getRecords()) {
            dates.add(yx.getGroupDate());
        }
        res.setData(dates);
        res.setTotal(checkPage.getTotal());
        return res;
    }

    @Override
    public PageData<String> GetGroupCheckData(String datasource, String groupDate, Integer pageIndex, Integer pageSize) {
        IPage<DataCheckYx> checkPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();

        if (datasource != null && !datasource.equals("")) {
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
        queryWrapper.groupBy("data_source,group_date");
        checkPage = mapper.selectPage(checkPage, queryWrapper);

        PageData<String> res = new PageData<>();
        List<String> dates = new ArrayList<>();
        for(DataCheckYx yx: checkPage.getRecords()) {
            dates.add(yx.getGroupDate() + "#" + yx.getDataSource());
        }
        res.setData(dates);
        res.setTotal(checkPage.getTotal());
        return res;
    }

    @Override
    public List<Map<String, Object>> GetDayActiveUser() {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(user_code) AS num,DATE(stime) AS datestr");
        queryWrapper.groupBy("datestr");
        List<Map<String, Object>> res = mapper.selectMaps(queryWrapper);
        return res;
    }

    @Override
    public List<Map<String, Object>> GetCxLGreen() {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(trip_check_code) AS num,trip_type");
        queryWrapper.groupBy("trip_type");
        List<Map<String, Object>> res = mapper.selectMaps(queryWrapper);
        return res;
    }

    @Override
    public List<Map<String, Object>> GetHourUser() {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(1) AS num,hour(stime) AS timestr");
        queryWrapper.groupBy("timestr");
        queryWrapper.orderByAsc("timestr");
        List<Map<String, Object>> res = mapper.selectMaps(queryWrapper);
        return res;
    }

    @Override
    public Integer GetUserCount() {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct user_code");
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetTripCount() {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public List<DataCheckYx> GetListByTime(String datasource, String stime, String etime) {
        QueryWrapper<DataCheckYx> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        queryWrapper.ge("stime", stime);
        queryWrapper.lt("stime", etime);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public void InsertBatch(List<DataCheckYx> lists) {
        int insertCount = 500;
        int i = 0, y = 0;
        for (DataCheckYx yx: lists) {
            if (i > 0 && (i % insertCount == 0 || i == lists.size() - 1)) {
                mapper.insertBatchSomeColumn(lists.subList(y * insertCount, i));
                y++;
            }
            i++;
        }
    }
}
