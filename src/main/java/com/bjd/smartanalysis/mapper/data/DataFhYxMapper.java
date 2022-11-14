package com.bjd.smartanalysis.mapper.data;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjd.smartanalysis.entity.data.DataFhYx;
import com.bjd.smartanalysis.entity.data.DataMonitorYx;

import java.util.List;

public interface DataFhYxMapper extends BaseMapper<DataFhYx> {
    Integer insertBatchSomeColumn(List<DataFhYx> lists);
}
