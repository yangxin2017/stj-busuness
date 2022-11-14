package com.bjd.smartanalysis.controller.param;

import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.param.PamProject;
import com.bjd.smartanalysis.service.param.PamProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "项目排放系数", tags = {"项目排放系数参数管理"})
@RestController
@RequestMapping("project")
public class PamProjectController {
    @Autowired
    private PamProjectService projectService;

    @PostMapping("add")
    @ApiOperation(value = "添加数据", notes = "添加数据")
    private ResponseData AddData(PamProject pam) {
        projectService.save(pam);
        return ResponseData.OK(pam);
    }

    @PostMapping("update")
    @ApiOperation(value = "更新数据", notes = "更新数据")
    private ResponseData UpdateData(PamProject pam) {
        projectService.updateById(pam);
        return ResponseData.OK(pam);
    }

    @GetMapping("data")
    @ApiOperation(value = "获取数据", notes = "获取数据")
    private ResponseData GetData() {
        List<PamProject> transferList = projectService.list();
        if(transferList.size() > 0) {
            return ResponseData.OK(transferList.get(0));
        }
        return ResponseData.OK(null);
    }
}
