package com.bjd.smartanalysis.service.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.data.UserCount;

public interface UserCountService extends IService<UserCount> {
    public UserCount GetByTime(String datestr);
}
