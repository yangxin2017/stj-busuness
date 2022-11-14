package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.data.HourCount;

public interface HourCountService extends IService<HourCount> {
    public HourCount GetByHour(String hstr);
}
