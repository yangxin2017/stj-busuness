package com.bjd.smartanalysis.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.entity.common.CmFile;

import java.util.List;
import java.util.Map;

public interface CmFileService extends IService<CmFile> {
    public static String READY = "等待存入";
    public static String ING = "数据存入中";
    public static String OVER = "数据存入完毕";

    public PageData SelectPage(Integer typeId, Integer pageIndex, Integer pageSize);
    public PageData SelectPage(Integer typeId, String datasource, Integer pageIndex, Integer pageSize);
    /*public PageData SelectPage(Integer typeId, String datasource, Integer pageIndex, Integer pageSize , String OrderBy);*/

    public List<CmFile> GetList(String dataSource, Integer typeId);

    public List<CmFile> GetAllReadyFile();
}
