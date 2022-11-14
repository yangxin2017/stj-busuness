package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.data.DataKey;

public interface DataKeyService extends IService<DataKey> {
    public DataKey GetByType(String keyType);
}
