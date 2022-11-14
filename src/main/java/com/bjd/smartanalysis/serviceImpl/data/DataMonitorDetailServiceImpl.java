package com.bjd.smartanalysis.serviceImpl.data;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.data.DataMonitorDetail;
import com.bjd.smartanalysis.mapper.data.DataMonitorDetailMapper;
import com.bjd.smartanalysis.service.data.DataMonitorDetailService;
import org.springframework.stereotype.Service;

@Service
public class DataMonitorDetailServiceImpl extends ServiceImpl<DataMonitorDetailMapper, DataMonitorDetail> implements DataMonitorDetailService {
}
