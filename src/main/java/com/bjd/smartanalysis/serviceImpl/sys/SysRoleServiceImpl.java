package com.bjd.smartanalysis.serviceImpl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.sys.SysRole;
import com.bjd.smartanalysis.mapper.sys.SysRoleMapper;
import com.bjd.smartanalysis.service.sys.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
