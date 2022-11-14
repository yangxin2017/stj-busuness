package com.bjd.smartanalysis.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjd.smartanalysis.entity.TestA;
import com.bjd.smartanalysis.mapper.TestAMapper;
import org.springframework.stereotype.Service;
import com.bjd.smartanalysis.service.TestAService;

@Service
public class TestAServiceImpl extends ServiceImpl<TestAMapper, TestA> implements TestAService {
}
