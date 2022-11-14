package com.bjd.smartanalysis.controller.param;

import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.entity.param.PamTransfer;
import com.bjd.smartanalysis.service.param.PamTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "里程转换系数", tags = {"里程转换系数参数管理"})
@RestController
@RequestMapping("cpf")
public class PamTransferController {
    @Autowired
    private PamTransferService transferService;

    @PostMapping("add")
    @ApiOperation(value = "添加数据", notes = "添加数据")
    private ResponseData AddData(PamTransfer pam) {
        transferService.save(pam);
        return ResponseData.OK(pam);
    }

    @PostMapping("update")
    @ApiOperation(value = "更新数据", notes = "更新数据")
    private ResponseData UpdateData(PamTransfer pam) {
        transferService.updateById(pam);
        return ResponseData.OK(pam);
    }

    @GetMapping("data")
    @ApiOperation(value = "获取数据", notes = "获取数据")
    private ResponseData GetData() {
        List<PamTransfer> transferList = transferService.list();
        if(transferList.size() > 0) {
            return ResponseData.OK(transferList.get(0));
        }
        return ResponseData.OK(null);
    }
}
