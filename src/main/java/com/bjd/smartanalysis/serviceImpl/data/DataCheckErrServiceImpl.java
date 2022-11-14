package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.data.DataCheckErr;
import com.bjd.smartanalysis.mapper.data.DataCheckErrMapper;
import com.bjd.smartanalysis.service.data.DataCheckErrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DataCheckErrServiceImpl extends ServiceImpl<DataCheckErrMapper, DataCheckErr> implements DataCheckErrService {
    @Autowired
    private DataCheckErrMapper mapper;

    @Override
    public DataCheckErr GetOnlyData(String userCode, Date stime, String slng, String slat) {
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_code", userCode);
        queryWrapper.eq("stime", stime);
        queryWrapper.eq("slng", slng);
        queryWrapper.eq("slat", slat);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public DataCheckErr GetOnlyData(String tripCheckCode) {
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trip_check_code", tripCheckCode);
        queryWrapper.last("limit 1");
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public Integer GetCountByFileId(Integer fileId) {
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountByFileId(Integer fileId, String errType) {
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        queryWrapper.eq("err_type", errType);
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetCountBySourceAndDate(String datasource, String groupdate) {
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        if(datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        if(groupdate != null && !groupdate.equals("")) {
            queryWrapper.eq("group_date", groupdate);
        }
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public PageData<DataCheckErr> GetDataByErrType(Integer fileId, String errType, Integer pageIndex, Integer pageSize) {
        IPage<DataCheckErr> checkPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        if (errType != null && !errType.equals("")) {
            queryWrapper.eq("err_type", errType);
        }
        checkPage = mapper.selectPage(checkPage, queryWrapper);
        PageData<DataCheckErr> res = new PageData<>();
        res.setTotal(checkPage.getTotal());
        res.setData(checkPage.getRecords());
        return res;
    }

    @Override
    public PageData<DataCheckErr> GetDataByErrType(Integer fileId, String errType, String tripType, String stime, String etime, Integer pageIndex, Integer pageSize) {
        IPage<DataCheckErr> checkPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_id", fileId);
        if (errType != null && !errType.equals("")) {
            queryWrapper.eq("err_type", errType);
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
        checkPage = mapper.selectPage(checkPage, queryWrapper);
        PageData<DataCheckErr> res = new PageData<>();
        res.setTotal(checkPage.getTotal());
        res.setData(checkPage.getRecords());


        return res;
    }

    @Override
    public void SetDataStatus(String dataId, String status) {
        UpdateWrapper<DataCheckErr> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", dataId);
        updateWrapper.set("data_status", status);
        mapper.update(null, updateWrapper);
    }

    @Override
    public Integer GetUserCount() {
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct user_code");
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public Integer GetTripCount() {
        QueryWrapper<DataCheckErr> queryWrapper = new QueryWrapper<>();
        return mapper.selectCount(queryWrapper);
    }
}
