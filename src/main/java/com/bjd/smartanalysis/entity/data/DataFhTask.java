package com.bjd.smartanalysis.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("data_fh_task")
public class DataFhTask {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("data_source")
    private String dataSource;

    @TableField("group_date")
    private String groupDate;

    @TableField("status")
    private String status;

    @TableField("fh_time")
    private Date fhTime;

    @TableField("yx_count")
    private Integer yxCount;

    @TableField("err_count")
    private Integer errCount;

    @TableField("total_count")
    private Integer totalCount;

    @TableField("yx_num")
    private Double yxNum;

    @TableField("err_num")
    private Double errNum;

    @TableField("total_num")
    private Double totalNum;
}
