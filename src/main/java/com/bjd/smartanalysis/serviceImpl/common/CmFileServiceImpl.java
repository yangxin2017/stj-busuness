package com.bjd.smartanalysis.serviceImpl.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.common.CmFile;
import com.bjd.smartanalysis.mapper.common.CmFileMapper;
import com.bjd.smartanalysis.service.common.CmFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CmFileServiceImpl extends ServiceImpl<CmFileMapper, CmFile> implements CmFileService {
    @Autowired
    private CmFileMapper mapper;

    @Override
    public PageData SelectPage(Integer typeId, Integer pageIndex, Integer pageSize) {
        IPage<CmFile> filePage = new Page<>(pageIndex, pageSize);
        QueryWrapper<CmFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("data_time");
        queryWrapper.eq("type_id", typeId);
        queryWrapper.orderByDesc("ctime");
        filePage = mapper.selectPage(filePage, queryWrapper);
        PageData res = new PageData();
        List<CmFile> datas = filePage.getRecords();
        res.setData(datas);
        res.setTotal(filePage.getTotal());
        return res;
    }

    @Override
    public PageData SelectPage(Integer typeId, String datasource, Integer pageIndex, Integer pageSize) {
        IPage<CmFile> filePage = new Page<>(pageIndex, pageSize);
        QueryWrapper<CmFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("data_time");
        queryWrapper.eq("type_id", typeId);
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        queryWrapper.orderByDesc("ctime");
        filePage = mapper.selectPage(filePage, queryWrapper);
        PageData res = new PageData();
        List<CmFile> datas = filePage.getRecords();
        res.setData(datas);
        res.setTotal(filePage.getTotal());
        return res;
    }

/*    @Override
    public PageData SelectPage(Integer typeId, String datasource, Integer pageIndex, Integer pageSize , String OrderBy) {
        IPage<CmFile> filePage = new Page<>(pageIndex, pageSize);
        QueryWrapper<CmFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        if (datasource != null && !datasource.equals("")) {
            queryWrapper.eq("data_source", datasource);
        }
        queryWrapper.orderByDesc("ctime");
        filePage = mapper.selectPage(filePage, queryWrapper);
        PageData res = new PageData();
        List<CmFile> datas = filePage.getRecords();
        res.setData(datas);
        res.setTotal(filePage.getTotal());
        return res;
    }*/

    @Override
    public List<CmFile> GetList(String dataSource, Integer typeId) {
        QueryWrapper<CmFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id", typeId);
        queryWrapper.eq("data_source", dataSource);
        return mapper.selectList(queryWrapper);
    }

    @Override
    public List<CmFile> GetAllReadyFile() {
        QueryWrapper<CmFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_status", CmFileService.READY);
        return mapper.selectList(queryWrapper);
    }
}
