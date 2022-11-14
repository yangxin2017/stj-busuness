package com.bjd.smartanalysis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sm_datatype")
public class DataType {
    @TableId
    private Integer id;

    @TableField("name")
    private String name;
}
