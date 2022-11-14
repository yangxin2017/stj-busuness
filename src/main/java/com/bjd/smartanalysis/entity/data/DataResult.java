package com.bjd.smartanalysis.entity.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("data_result")
public class DataResult {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("data_source")
    private String dataSource;

    @TableField("group_date")
    private String groupDate;

    @TableField("trip_type")
    private String tripType;

    @TableField("bx_sb")
    private Double bxSb;

    @TableField("qx_sb")
    private Double qxSb;

    @TableField("gj_sb")
    private Double gjSb;

    @TableField("dt_sb")
    private Double dtSb;

    @TableField("bx_ff")
    private Double bxFf;

    @TableField("qx_ff")
    private Double qxFf;

    @TableField("gj_ff")
    private Double gjFf;

    @TableField("dt_ff")
    private Double dtFf;
}
