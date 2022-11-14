package com.bjd.smartanalysis.excellistener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.extension.service.IService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DataExcelListener<T> extends AnalysisEventListener<T> {
    private IService service;
    private List<T> list = new ArrayList<>();
    private String type;

    public DataExcelListener(IService service, String type) {
        this.service = service;
        this.type = type;
    }

    @Override
    public void invoke(T o, AnalysisContext analysisContext) {
        try {
            Class curClass = o.getClass();
            Field curField = curClass.getDeclaredField("type");
            if (curField != null) {
                curField.setAccessible(true);
                curField.set(o, this.type);
            }
        }catch (Exception ex){
        }
        list.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        service.saveBatch(list);
    }
}