package com.bjd.smartanalysis.controller.main;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.PageData;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.common.CmFile;
import com.bjd.smartanalysis.entity.data.*;
import com.bjd.smartanalysis.entity.flow.SysFlow;
import com.bjd.smartanalysis.service.common.CmFileService;
import com.bjd.smartanalysis.service.data.*;
import com.bjd.smartanalysis.service.flow.SysFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Api(value = "首页统计", tags = {"首页统计"})
@RestController
@RequestMapping("main")
public class MainPageController {
    @Autowired
    private DataMonitorYxService monitorYxService;
    @Autowired
    private DataMonitorErrService monitorErrService;

    @Autowired
    private DataCheckYxService yxService;
    @Autowired
    private DataCheckErrService errService;

    @Autowired
    private DataFhYxService fhYxService;
    @Autowired
    private DataFhErrService fhErrService;

    @Autowired
    private SysFlowService flowService;

    @Autowired
    private DataFhTaskService fhTaskService;
    @Autowired
    private DataResultService resultService;
    @Autowired
    private CmFileService fileService;
    @Autowired
    private UserCountService userCountService;
    @Autowired
    private TriptypeCountService triptypeCountService;

    private Boolean isFixedDate = false;

    @GetMapping("total")
    @ApiOperation(value = "累计CO2减排量", notes = "累计CO2减排量")
    private ResponseData GetTotal() {

        List<DataResult> results = resultService.list();
        Double sb = 0.0;
        for(DataResult r: results) {
            sb += r.getBxSb() + r.getQxSb() + r.getGjSb() + r.getDtSb();
        }

        List<UserCount> res = userCountService.list();
        Integer num = 0;
        for(UserCount uc: res) {
           Integer n = Integer.parseInt(uc.getNum());
           num += n;
        }

        List<TriptypeCount> restip = triptypeCountService.list();
        Integer numttip = 0;
        for(TriptypeCount tc: restip){
            Integer n = Integer.parseInt(tc.getNum());
            numttip += n;
        }

        Integer userCount = num;
        Integer tripCount = numttip;
        Double cNum = sb;
        Double money = 0.0;
        JSONObject obj = new JSONObject();
        obj.put("userCount", userCount);
        obj.put("tripCount", tripCount);
        obj.put("cNum", cNum);
        obj.put("money", money);
        return ResponseData.OK(obj);
    }

    @GetMapping("month")
    @ApiOperation(value = "月度CO2减排量", notes = "月度CO2减排量")
    private ResponseData GetMonthTotal() {
        Date cur = DateUtil.offsetMonth(new Date(), -1);
        String curMonth = DateUtil.format(cur, "yyyy-MM");

        DataResult dr = resultService.GetNewestOne();
        if(dr != null) {
            curMonth = dr.getGroupDate();
            cur = DateUtil.parse(curMonth, "yyyy-MM");
        }

        if (isFixedDate) {
            curMonth = "2022-07";
        }
        String lastMonth = DateUtil.format(DateUtil.offsetMonth(cur, -1), "yyyy-MM");
        String lastYearMonth = DateUtil.format(DateUtil.offsetMonth(cur, -12), "yyyy-MM");


        List<DataResult> results = resultService.GetEqualList(null, curMonth);
        Double sbcur = 0.0;
        for(DataResult r: results) {
            sbcur += r.getBxSb() + r.getQxSb() + r.getGjSb() + r.getDtSb();
        }

        List<DataResult> lastResults = resultService.GetEqualList(null, lastMonth);
        Double sblast = 0.0;
        for(DataResult r: lastResults) {
            sblast += r.getBxSb() + r.getQxSb() + r.getGjSb() + r.getDtSb();
        }

        List<DataResult> lmResults = resultService.GetEqualList(null, lastYearMonth);
        Double sblm = 0.0;
        for(DataResult r: lmResults) {
            sblm += r.getBxSb() + r.getQxSb() + r.getGjSb() + r.getDtSb();
        }

        Double curnum = sbcur;
        Double last = sblast;
        Double lm = sblm;

        JSONObject obj = new JSONObject();
        obj.put("curMonth", curnum);
        obj.put("lastMonth", last);
        obj.put("lastYearMonth", lm);
        obj.put("cur_str", Integer.parseInt(curMonth.split("-")[1]) + "月");
        obj.put("last_str", Integer.parseInt(lastMonth.split("-")[1]) + "月");

        lastYearMonth = lastYearMonth.replace("-0", "-");
        obj.put("ly_str", lastYearMonth.replace("-", "年") + "月");
        return ResponseData.OK(obj);
    }

    @GetMapping("ctypes")
    @ApiOperation(value = "当月不同出行模式CO2减排占比", notes = "当月不同出行模式CO2减排占比")
    private ResponseData GetCByTypes() {
        Date cur = DateUtil.offsetMonth(new Date(), -1);
        String curMonth = DateUtil.format(cur, "yyyy-MM");

        DataResult dr = resultService.GetNewestOne();
        if(dr != null) {
            curMonth = dr.getGroupDate();
        }

        Double dt = 0.0, gj = 0.0, zxc = 0.0, bx = 0.0;
        if (isFixedDate) {
            curMonth = "2022-07";
        }

        List<DataResult> results = resultService.GetEqualList(null, curMonth);
        for(DataResult r: results) {
            dt += r.getDtSb();
            gj += r.getGjSb();
            zxc += r.getQxSb();
            bx += r.getBxSb();
        }

        JSONObject obj = new JSONObject();
        obj.put("dt", dt);
        obj.put("gj", gj);
        obj.put("zxc", zxc);
        obj.put("bx", bx);

        obj.put("cur_str", curMonth.split("-")[1] + "月");
        return ResponseData.OK(obj);
    }

    @GetMapping("typedatas")
    @ApiOperation(value = "当月CO2减排量数据报送审核情况", notes = "当月CO2减排量数据报送审核情况")
    private ResponseData GetDataConditionGD(String datasource) {
        Date cur = DateUtil.offsetMonth(new Date(), -1);
        String curMonth = DateUtil.format(cur, "yyyy-MM");

        DataResult dr = resultService.GetNewestOne();
        if(dr != null) {
            curMonth = dr.getGroupDate();
        }

        if (isFixedDate) {
            curMonth = "2022-07";
        }

        Integer bs = 0;
        Integer dsf = 0;
        Integer fh = 0;

        List<DataFhTask> tasks = fhTaskService.GetList(datasource);
        Double yxNum = 0.0;
        Double errNum = 0.0;
        for(DataFhTask t: tasks) {
            yxNum += t.getTotalNum() != null ? t.getTotalNum() : 0.0;
            errNum += t.getErrNum() != null ? t.getErrNum() : 0.0;

            bs += t.getTotalCount();
            fh += t.getYxCount();
            dsf += t.getErrCount();
        }

        Double ff = 0.0;
        List<DataResult> results = resultService.GetEqualList(datasource, curMonth);
        for(DataResult r: results) {
            ff += r.getBxFf() + r.getQxFf() + r.getQxFf() + r.getDtFf();
        }

        JSONObject obj = new JSONObject();
        obj.put("bs", bs);
        obj.put("dsf", dsf);
        obj.put("fh", fh);
        obj.put("yxNum", yxNum);
        obj.put("errNum", errNum);
        obj.put("ff", ff);
        obj.put("monthstr", curMonth);
        return ResponseData.OK(obj);
    }
}
