package com.bjd.smartanalysis.controller.main;

import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.data.HourCount;
import com.bjd.smartanalysis.entity.data.TriptypeCount;
import com.bjd.smartanalysis.entity.data.UserCount;
import com.bjd.smartanalysis.service.data.DataCheckYxService;
import com.bjd.smartanalysis.service.data.HourCountService;
import com.bjd.smartanalysis.service.data.TriptypeCountService;
import com.bjd.smartanalysis.service.data.UserCountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(value = "数据查询（审核数据）", tags = {"数据查询（审核数据）"})
@RestController
@RequestMapping("statics")
public class DataStaticsController {
    @Autowired
    private DataCheckYxService checkYxService;

    @Autowired
    private UserCountService userCountService;
    @Autowired
    private TriptypeCountService triptypeCountService;
    @Autowired
    private HourCountService hourCountService;

    @GetMapping("total")
    @ApiOperation(value = "每日活跃用户", notes = "每日活跃用户")
    private ResponseData GetTotal() {
        List<UserCount> res = userCountService.list();
        return ResponseData.OK(res);
    }

    @GetMapping("green")
    @ApiOperation(value = "绿色出行量", notes = "绿色出行量")
    private ResponseData GetGreenTrip() {
        List<TriptypeCount> res = triptypeCountService.list();
        return ResponseData.OK(res);
    }

    @GetMapping("hour")
    @ApiOperation(value = "小时统计绿色出行", notes = "小时统计绿色出行")
    private ResponseData GetHourGreenTrip() {
        List<HourCount> res = hourCountService.list();
        return ResponseData.OK(res);
    }

}
