package com.bjd.smartanalysis.mapper.data;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjd.smartanalysis.entity.data.DataMonitorYx;

import java.util.List;

public interface DataMonitorYxMapper extends BaseMapper<DataMonitorYx> {
    Integer insertBatchSomeColumn(List<DataMonitorYx> lists);
}
