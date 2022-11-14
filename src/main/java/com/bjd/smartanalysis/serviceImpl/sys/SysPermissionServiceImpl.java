package com.bjd.smartanalysis.serviceImpl.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.sys.SysPermission;
import com.bjd.smartanalysis.mapper.sys.SysPermissionMapper;
import com.bjd.smartanalysis.service.sys.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    @Autowired
    private SysPermissionMapper mapper;

    @Override
    public List<SysPermission> getPermsByRoleId(Integer roleId) {
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        return mapper.selectList(queryWrapper);
    }
}
