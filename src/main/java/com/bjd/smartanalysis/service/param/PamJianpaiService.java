package com.bjd.smartanalysis.service.param;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjd.smartanalysis.entity.param.PamJianpai;

import java.util.List;

public interface PamJianpaiService extends IService<PamJianpai> {
    public List<PamJianpai> GetByType(String type);
}
