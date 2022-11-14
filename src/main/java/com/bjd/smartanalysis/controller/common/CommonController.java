package com.bjd.smartanalysis.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.service.common.CmFileTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "基本数据", tags = {"基本数据"})
@RestController
@RequestMapping("base")
public class CommonController {
    @Autowired
    private CmFileTypeService fileTypeService;

    @GetMapping("filetypes")
    @ApiOperation(value = "获取上传文件列表", notes = "获取上传文件列表")
    public ResponseData GetFileTypeList() {
        return ResponseData.OK(fileTypeService.list());
    }

    @GetMapping("errtypes")
    @ApiOperation(value = "获取上传数据错误类型", notes = "获取上传数据错误类型")
    public ResponseData GetErrTypeList() {
        List<JSONObject> res = new ArrayList<>();

        JSONObject o1 = new JSONObject();
        o1.put("label", "数值错误");
        o1.put("value", "ERROR");
        res.add(o1);

        JSONObject o2 = new JSONObject();
        o2.put("label", "数值重复");
        o2.put("value", "REPEAT");
        res.add(o2);

        JSONObject o3 = new JSONObject();
        o3.put("label", "空数据");
        o3.put("value", "NULL");
        res.add(o3);

        return ResponseData.OK(res);
    }

    @GetMapping("depts")
    @ApiOperation(value = "申报单位", notes = "申报单位")
    public ResponseData GetDepts(){
        List<JSONObject> res = new ArrayList<>();

        JSONObject o1 = new JSONObject();
        o1.put("label", "高德");
        o1.put("value", "高德");
        res.add(o1);

        JSONObject o2 = new JSONObject();
        o2.put("label", "百度");
        o2.put("value", "百度");
        res.add(o2);
        return ResponseData.OK(res);
    }

    @GetMapping("triptypes")
    @ApiOperation(value = "出行方式", notes = "出行方式")
    public ResponseData GetTripTypes(){
        List<JSONObject> res = new ArrayList<>();

        JSONObject o1 = new JSONObject();
        o1.put("label", "步行");
        o1.put("value", "walk");
        res.add(o1);

        JSONObject o2 = new JSONObject();
        o2.put("label", "自行车");
        o2.put("value", "cycle");
        res.add(o2);

        JSONObject o3 = new JSONObject();
        o3.put("label", "公交");
        o3.put("value", "GJ");
        res.add(o3);

        JSONObject o4 = new JSONObject();
        o4.put("label", "地铁");
        o4.put("value", "DT");
        res.add(o4);
        return ResponseData.OK(res);
    }

    @GetMapping("statustypes")
    @ApiOperation(value = "数据状态", notes = "数据状态")
    public ResponseData GetDataStatusTypes(){
        List<JSONObject> res = new ArrayList<>();

        JSONObject o1 = new JSONObject();
        o1.put("label", "删除");
        o1.put("value", "删除");
        res.add(o1);

        JSONObject o2 = new JSONObject();
        o2.put("label", "忽略");
        o2.put("value", "忽略");
        res.add(o2);

        JSONObject o3 = new JSONObject();
        o3.put("label", "待确认");
        o3.put("value", "待确认");
        res.add(o3);

        return ResponseData.OK(res);
    }

}
