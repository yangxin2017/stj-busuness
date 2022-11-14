package com.bjd.smartanalysis.controller.param;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.bjd.smartanalysis.common.ResponseData;
import com.bjd.smartanalysis.common.UploadUtil;
import com.bjd.smartanalysis.entity.common.CmFile;
import com.bjd.smartanalysis.entity.common.CmFileType;
import com.bjd.smartanalysis.entity.param.PamCpf;
import com.bjd.smartanalysis.service.common.CmFileService;
import com.bjd.smartanalysis.service.common.CmFileTypeService;
import com.bjd.smartanalysis.service.param.PamCpfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Api(value = "碳排放", tags = {"碳排放参数管理"})
@RestController
@RequestMapping("cpf")
public class PamCpfController {
    @Value("${file.upload.path}")
    private String basePath;

    @Autowired
    private CmFileTypeService fileTypeService;
    @Autowired
    private CmFileService fileService;
    @Autowired
    private PamCpfService cpfService;

    @PostMapping("upload")
    @ApiOperation(value = "上传数据", notes = "上传数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件流对象", required = true, dataType = "__File")})
    private ResponseData UploadData(@RequestParam("file") MultipartFile file, Integer bid){
        if (file.isEmpty() || bid == null) {
            return ResponseData.FAIL("没有选择文件");
        }

        CmFileType dtype = fileTypeService.getById(bid);
        if (dtype == null) {
            return ResponseData.FAIL("数据类型不存在");
        }

        if(!UploadUtil.SaveFileToDatabase("", file, PamCpf.class, cpfService)){
            return ResponseData.FAIL("数据导入失败");
        }

        CmFile df = UploadUtil.Upload(file, basePath, bid);
        if (df != null) {
            fileService.save(df);
        } else {
            return ResponseData.FAIL("文件上传失败");
        }
        return ResponseData.OK(null);
    }

    @GetMapping("export")
    @ApiOperation(value = "导出所有数据", notes = "导出所有数据")
    public void ExportExcel(HttpServletResponse response) {
        List<PamCpf> data = cpfService.list();
        try {
            String filename = "碳排放参数导出_" + DateUtil.format(new Date(), "YYYY_MM_DD_HH_mm_ss") + ".xlsx";
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            EasyExcel.write(response.getOutputStream(), PamCpf.class).sheet().doWrite(data);
        }catch (Exception e){
            System.out.println("导出失败！");
            e.printStackTrace();
        }
    }
}
