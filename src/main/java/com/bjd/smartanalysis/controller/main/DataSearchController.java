package com.bjd.smartanalysis.controller.main;


import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.data.DataFhTask;
import com.bjd.smartanalysis.entity.data.DataResult;
import com.bjd.smartanalysis.entity.flow.SysFlow;
import com.bjd.smartanalysis.service.data.DataFhErrService;
import com.bjd.smartanalysis.service.data.DataFhTaskService;
import com.bjd.smartanalysis.service.data.DataFhYxService;
import com.bjd.smartanalysis.service.data.DataResultService;
import com.bjd.smartanalysis.service.flow.SysFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "数据查询（审核数据）", tags = {"数据查询（审核数据）"})
@RestController
@RequestMapping("search")
public class DataSearchController {
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

    @GetMapping("total")
    @ApiOperation(value = "累计", notes = "累计")
    private ResponseData GetTotal() {
//        Double sb = fhYxService.GetCNumber(null, null) + fhErrService.GetCNumber(null, null) + fhErrService.GetJZNumber(null, null);
        List<DataResult> results = resultService.list();

        Double ff = 0.0;
        Double sb = 0.0;
        for(DataResult r: results) {
            sb += r.getBxSb() + r.getQxSb() + r.getGjSb() + r.getDtSb();
            ff += r.getBxFf() + r.getQxFf() + r.getQxFf() + r.getDtFf();
        }

        JSONObject obj = new JSONObject();
        obj.put("sb", sb);
        obj.put("ff", ff);
        return ResponseData.OK(obj);
    }

    @GetMapping("datatype")
    @ApiOperation(value = "获取申报发放量数据", notes = "获取申报发放量数据")
    private ResponseData GetTotal(String datasource) {
        Double sb_dt = 0.0, sb_gj = 0.0, sb_zxc = 0.0, sb_bx = 0.0;
        Double ff_dt = 0.0, ff_gj = 0.0, ff_zxc = 0.0, ff_bx = 0.0;

        List<DataResult> results = resultService.GetList(datasource, null, null);
        for (DataResult r: results) {
            sb_dt += r.getDtSb();
            sb_gj += r.getGjSb();
            sb_zxc += r.getQxSb();
            sb_bx += r.getBxSb();

            ff_bx += r.getBxFf();
            ff_dt += r.getDtFf();
            ff_gj += r.getGjFf();
            ff_zxc += r.getQxFf();
        }

        JSONObject obj = new JSONObject();
        obj.put("sb_dt", sb_dt);
        obj.put("sb_gj", sb_gj);
        obj.put("sb_zxc", sb_zxc);
        obj.put("sb_bx", sb_bx);

        obj.put("ff_dt", ff_dt);
        obj.put("ff_gj", ff_gj);
        obj.put("ff_zxc", ff_zxc);
        obj.put("ff_bx", ff_bx);
        return ResponseData.OK(obj);
    }

    @GetMapping("search")
    @ApiOperation(value = "查询累计", notes = "查询累计")
    private ResponseData GetSearchTotal(String datasource, String triptype, String stime, String etime) {
        Double sb = 0.0;
        Double ff = 0.0;
        List<DataResult> results = resultService.GetList(datasource, stime, etime);
        for(DataResult r: results) {
            if (triptype != null && !triptype.equals("")) {
                if (triptype.equals("walk")) {
                    sb += r.getBxSb();
                    ff += r.getBxFf();
                } else if (triptype.equals("cycle") || triptype.equals("ecycle")) {
                    sb += r.getQxSb();
                    ff += r.getQxFf();
                } else if (triptype.equals("GJ")) {
                    sb += r.getGjSb();
                    ff += r.getGjFf();
                } else if (triptype.equals("DT")) {
                    sb += r.getDtSb();
                    ff += r.getDtFf();
                }
            } else {
                sb += r.getBxSb() + r.getQxSb() + r.getGjSb() + r.getDtSb();
                ff += r.getBxFf() + r.getQxFf() + r.getQxFf() + r.getDtFf();
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("sb", sb);
        obj.put("ff", ff);
        return ResponseData.OK(obj);
    }
}
