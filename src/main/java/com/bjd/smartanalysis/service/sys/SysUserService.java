package com.bjd.smartanalysis.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.sys.SysUser;

public interface SysUserService extends IService<SysUser> {
    public SysUser getUserByName(String username);
}
