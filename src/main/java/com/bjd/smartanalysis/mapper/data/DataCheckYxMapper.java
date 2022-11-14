package com.bjd.smartanalysis.mapper.data;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjd.smartanalysis.entity.data.DataCheckYx;
import com.bjd.smartanalysis.entity.data.DataFhYx;

import java.util.List;

public interface DataCheckYxMapper extends BaseMapper<DataCheckYx> {
    Integer insertBatchSomeColumn(List<DataCheckYx> lists);
}
