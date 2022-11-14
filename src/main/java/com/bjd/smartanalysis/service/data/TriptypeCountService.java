package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.data.TriptypeCount;

public interface TriptypeCountService extends IService<TriptypeCount> {
    public TriptypeCount GetByType(String tripType);
}
