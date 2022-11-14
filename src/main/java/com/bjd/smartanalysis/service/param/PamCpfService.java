package com.bjd.smartanalysis.service.param;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.param.PamCpf;

import java.util.List;

public interface PamCpfService extends IService<PamCpf> {
    PamCpf GetOneByTime(String time);
}
