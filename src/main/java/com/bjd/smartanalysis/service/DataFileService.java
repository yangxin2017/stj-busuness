package com.bjd.smartanalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.DataFile;

import java.util.List;

public interface DataFileService extends IService<DataFile> {
    public List<DataFile> GetFileByBid(Integer bid);
}
