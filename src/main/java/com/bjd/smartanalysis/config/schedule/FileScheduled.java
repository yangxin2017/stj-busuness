package com.bjd.smartanalysis.config.schedule;

import com.bjd.smartanalysis.entity.common.CmFile;
import com.bjd.smartanalysis.entity.data.DataCheckYx;
import com.bjd.smartanalysis.entity.data.DataKey;
import com.bjd.smartanalysis.service.common.CmFileService;
import com.bjd.smartanalysis.service.data.DataCheckYxService;
import com.bjd.smartanalysis.service.data.DataKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class FileScheduled {
    @Autowired
    private CmFileService fileService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private DataCheckYxService yxService;

    @Autowired
    private DataKeyService keyService;

    @Scheduled(fixedRate=10000)
    private void configureTasks() throws IOException {
        List<CmFile> files = fileService.GetAllReadyFile();
        for(CmFile f: files) {
            System.out.println("开始插入数据：" + f.getFileName());

            f.setDataStatus(CmFileService.ING);
            fileService.updateById(f);

            taskService.excutVoidTask(f);
        }
    }

    @PostConstruct
    public void StartUp() {
        System.out.println("Start....");
        System.out.println("ok");
    }
}
