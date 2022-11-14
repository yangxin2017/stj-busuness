package com.bjd.smartanalysis.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.sys.SysPermission;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {
    public List<SysPermission> getPermsByRoleId(Integer roleId);
}
